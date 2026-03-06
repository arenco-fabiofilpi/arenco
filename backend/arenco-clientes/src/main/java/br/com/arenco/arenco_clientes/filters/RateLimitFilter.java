package br.com.arenco.arenco_clientes.filters;

import com.github.benmanes.caffeine.cache.Cache;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import org.jspecify.annotations.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class RateLimitFilter extends OncePerRequestFilter {

  private final Bandwidth bandwidth;
  private final String[] includePatterns;
  private final Cache<@NonNull String, Bucket> cache;
  private final AntPathMatcher matcher = new AntPathMatcher();

  public RateLimitFilter(
      final Cache<@NonNull String, Bucket> cache,
      final Bandwidth bandwidth,
      final String includePaths) {
    this.cache = cache;
    this.bandwidth = bandwidth;
    this.includePatterns =
        Arrays.stream(includePaths.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toArray(String[]::new);
  }

  @Override
  protected boolean shouldNotFilter(final HttpServletRequest request) {
    final String path = request.getRequestURI();
    // filtra apenas os paths configurados
    if (includePatterns.length == 0) return false; // filtra tudo
    for (String p : includePatterns) {
      if (matcher.match(p, path)) return false;
    }
    return true; // não filtra
  }

  @Override
  protected void doFilterInternal(
      @NonNull final HttpServletRequest req,
      @NonNull final HttpServletResponse res,
      @NonNull final FilterChain chain)
      throws ServletException, IOException {

    final String key = resolveKey(req);
    final Bucket bucket =
        cache.asMap().computeIfAbsent(key, k -> Bucket.builder().addLimit(bandwidth).build());

    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
    if (probe.isConsumed()) {
      // Cabeçalhos informativos (padrão de mercado)
      res.setHeader("X-Rate-Limit-Limit", String.valueOf(bandwidth.getCapacity()));
      res.setHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
      long nanosToRefill = probe.getNanosToWaitForRefill();
      if (nanosToRefill > 0) {
        long seconds = Duration.ofNanos(nanosToRefill).toSeconds();
        res.setHeader(
            "X-Rate-Limit-Reset", String.valueOf(Instant.now().getEpochSecond() + seconds));
      }
      chain.doFilter(req, res);
    } else {
      long waitSec = Duration.ofNanos(probe.getNanosToWaitForRefill()).toSeconds();
      res.setStatus(429);
      res.setHeader("Retry-After", String.valueOf(waitSec));
      res.setHeader("X-Rate-Limit-Limit", String.valueOf(bandwidth.getCapacity()));
      res.setHeader("X-Rate-Limit-Remaining", "0");
      res.setHeader("X-Rate-Limit-Reset", String.valueOf(Instant.now().getEpochSecond() + waitSec));
      res.setContentType("application/json");
      res.getWriter()
          .write(
              """
        {"status":429,"error":"Too Many Requests","message":"Rate limit exceeded. Try again later."}
        """);
    }
  }

  private String resolveKey(final HttpServletRequest req) {
    // 1) Cookies: SESSION_ID > PRE_SESSION_ID
    final String session = readCookie(req, "SESSION_ID");
    if (session != null && !session.isBlank()) return "sid:" + session;

    final String pre = readCookie(req, "PRE_SESSION_ID");
    if (pre != null && !pre.isBlank()) return "presid:" + pre;

    // 2) Fallback: IP do cliente (considera NGINX / proxies)
    // Tenta Forwarded (RFC 7239)
    final String forwarded = req.getHeader("Forwarded");
    if (forwarded != null && !forwarded.isBlank()) {
      // Ex.: Forwarded: for=203.0.113.195, for=198.51.100.17
      final String ip = extractFirstForwardedFor(forwarded);
      if (ip != null) return "ip:" + ip;
    }

    // Tenta X-Forwarded-For (cadeia "client, proxy1, proxy2")
    final String xff = req.getHeader("X-Forwarded-For");
    if (xff != null && !xff.isBlank()) {
      final String ip = xff.split(",")[0].trim();
      if (!ip.isEmpty()) return "ip:" + ip;
    }

    // Sem proxy reverso: remoteAddr
    return "ip:" + req.getRemoteAddr();
  }

  private String readCookie(final HttpServletRequest req, final String name) {
    if (req.getCookies() == null) return null;
    for (final Cookie c : req.getCookies()) {
      if (name.equals(c.getName())) {
        return c.getValue();
      }
    }
    return null;
  }

  private String extractFirstForwardedFor(final String forwardedHeader) {
    // Pega primeiro "for=" do cabeçalho Forwarded
    // Forwarded: for=192.0.2.60;proto=http;by=203.0.113.43
    for (final String part : forwardedHeader.split(",")) {
      for (final String kv : part.split(";")) {
        final String s = kv.trim();
        if (s.toLowerCase().startsWith("for=")) {
          String v = s.substring(4).trim();
          // pode vir entre aspas ou com ip:port
          v = v.replace("\"", "");
          int idx = v.indexOf(':'); // remove porta se houver
          return idx > 0 ? v.substring(0, idx) : v;
        }
      }
    }
    return null;
  }
}
