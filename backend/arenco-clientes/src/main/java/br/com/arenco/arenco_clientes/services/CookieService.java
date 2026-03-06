package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.UserModel;
import jakarta.servlet.http.HttpServletResponse;

public interface CookieService {
  void createPreSessionCookie(final HttpServletResponse response, final UserModel user);

  void createSessionCookie(final HttpServletResponse response, final UserModel userModel);

  void removePreSessionCookie(final HttpServletResponse response);

  void removeSessionCookie(final HttpServletResponse response);
}
