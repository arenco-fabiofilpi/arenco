package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.config.properties.AuthenticationProperties;
import br.com.arenco.arenco_clientes.constants.ArencoAuthLibConstants;
import br.com.arenco.arenco_clientes.services.ArencoPreSessionService;
import br.com.arenco.arenco_clientes.services.ArencoSessionService;
import br.com.arenco.arenco_clientes.services.CookieService;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.entities.UserModel;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CookieServiceImpl implements CookieService {
  private final AuthenticationProperties authenticationProperties;
  private final ArencoSessionService arencoSessionService;
  private final ArencoPreSessionService arencoPreSessionService;

  @Override
  public void removePreSessionCookie(final HttpServletResponse response) {
    setCookie("", ArencoAuthLibConstants.PRE_SESSION_ID_COOKIE_NAME, 0, response);
  }

  @Override
  public void removeSessionCookie(final HttpServletResponse response) {
    setCookie("", ArencoAuthLibConstants.SESSION_ID_COOKIE_NAME, 0, response);
  }

  @Override
  public void createPreSessionCookie(final HttpServletResponse response, final UserModel user) {
    final var preSessionRecord = arencoPreSessionService.createPreSession(user.getId());
    final var rawUuid = preSessionRecord.rawUuid();
    final var modelExpiration = preSessionRecord.authenticatedPreSessionModel().getExpiresAt();
    final var maxAge = ArencoDateUtils.getCookieMaxAge(modelExpiration);
    setCookie(rawUuid, ArencoAuthLibConstants.PRE_SESSION_ID_COOKIE_NAME, maxAge, response);
  }

  @Override
  public void createSessionCookie(final HttpServletResponse response, final UserModel userModel) {
    final var sessionRecord = arencoSessionService.createSession(userModel.getId());
    final var rawUuid = sessionRecord.rawUuid();
    final Instant modelExpiration = sessionRecord.sessionModel().getExpiresAt();
    final var maxAge = ArencoDateUtils.getCookieMaxAge(modelExpiration);
    setCookie(rawUuid, ArencoAuthLibConstants.SESSION_ID_COOKIE_NAME, maxAge, response);
  }

  private void setCookie(
      final String sessionId,
      final String cookieName,
      final int expiry,
      final HttpServletResponse response) {

    final Cookie cookie = new Cookie(cookieName, sessionId);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(expiry);

    if (authenticationProperties.isLocalEnvironment()) {
      cookie.setSecure(false); // em HTTP local
      cookie.setAttribute("SameSite", "Lax"); // ok para same-site entre portas
    } else {
      // Produção: HTTPS obrigatório
      cookie.setSecure(true);
      cookie.setAttribute("SameSite", "Strict");
    }

    response.addCookie(cookie);
  }
}
