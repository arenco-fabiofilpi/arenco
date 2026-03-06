package br.com.arenco.arenco_clientes.filters;

import br.com.arenco.arenco_clientes.context.RequestContext;
import br.com.arenco.arenco_clientes.context.RequestData;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ArencoRequestContextFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      final HttpServletRequest httpRequest = (HttpServletRequest) request;
      final String ip = getClientIp(httpRequest);
      final String userAgent = httpRequest.getHeader("User-Agent");

      final RequestData data = new RequestData(ip, userAgent);
      RequestContext.set(data);

      // continue a cadeia de filtros
      chain.doFilter(request, response);
    } finally {
      // limpa o ThreadLocal após o processamento
      RequestContext.clear();
    }
  }

  private String getClientIp(final HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    } else {
      ip = ip.split(",")[0];
    }
    return ip;
  }
}
