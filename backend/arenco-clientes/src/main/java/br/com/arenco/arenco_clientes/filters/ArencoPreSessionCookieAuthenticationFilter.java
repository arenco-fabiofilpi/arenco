package br.com.arenco.arenco_clientes.filters;

import br.com.arenco.arenco_clientes.constants.ArencoAuthLibConstants;
import br.com.arenco.arenco_clientes.context.ArencoAuthenticatedPreSessionContext;
import br.com.arenco.arenco_clientes.exceptions.InvalidSessionException;
import br.com.arenco.arenco_clientes.services.ArencoPreSessionService;
import br.com.arenco.arenco_clientes.services.CookieService;
import br.com.arenco.arenco_clientes.services.PreSessionOtpService;
import br.com.arenco.arenco_clientes.entities.AuthenticatedPreSessionModel;
import br.com.arenco.arenco_clientes.entities.PreSessionOtpModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArencoPreSessionCookieAuthenticationFilter extends OncePerRequestFilter {
  private final ArencoPreSessionService arencoPreSessionService;
  private final PreSessionOtpService preSessionOtpService;
  private final CookieService cookieService;

  @Override
  protected void doFilterInternal(
      final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain filterChain)
      throws ServletException, IOException {
    final Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (ArencoAuthLibConstants.PRE_SESSION_ID_COOKIE_NAME.equals(cookie.getName())) {
          try {
            validatePreSessionCookie(cookie);
          } catch (final RuntimeException e) {
            log.error("Removendo Pre Session Cookie", e);
            cookieService.removeSessionCookie(response);
          }
        }
      }
    }
    filterChain.doFilter(request, response);
  }

  private void validatePreSessionCookie(final Cookie cookie) {
    final String sessionId = cookie.getValue();
    final var preSessionModel = getPreSessionModel(sessionId);
    final UserModel user = arencoPreSessionService.getUserBySessionModel(preSessionModel);
    final var roles = List.of(new SimpleGrantedAuthority("ROLE_AUTHENTICATED_PRE_SESSION"));

    final ArencoAuthenticatedPreSessionContext auth =
        new ArencoAuthenticatedPreSessionContext(
            user, null, roles, preSessionModel, getOneTimePasswordAtomicReference(preSessionModel));
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  private AtomicReference<PreSessionOtpModel> getOneTimePasswordAtomicReference(
      final AuthenticatedPreSessionModel preSessionModel) {
    final var otpOptional = preSessionOtpService.findByPreSessionId(preSessionModel.getHashedId());
    if (otpOptional.isPresent()) {
      final var otp = otpOptional.get();
      return new AtomicReference<>(otp);
    }
    return new AtomicReference<>();
  }

  private AuthenticatedPreSessionModel getPreSessionModel(String sessionId) {
    final var optionalSession = arencoPreSessionService.findPreSessionModel(sessionId);
    if (optionalSession.isEmpty()) {
      logger.error("validatePreSessionCookie - Pre-session cookie invalid");
      throw new InvalidSessionException();
    }
    return optionalSession.get();
  }
}
