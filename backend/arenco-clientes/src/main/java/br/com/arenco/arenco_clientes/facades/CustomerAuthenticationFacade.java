package br.com.arenco.arenco_clientes.facades;

import jakarta.servlet.http.HttpServletResponse;

public interface CustomerAuthenticationFacade {
  void customerLogin(final String username);

  void validateOtp(final String username, final String otp);

  void authenticateOtp(final String username, final String otp, final HttpServletResponse response);
}
