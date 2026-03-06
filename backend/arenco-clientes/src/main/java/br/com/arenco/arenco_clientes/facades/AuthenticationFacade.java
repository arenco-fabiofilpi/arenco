package br.com.arenco.arenco_clientes.facades;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public interface AuthenticationFacade {
  HttpStatus login(
      final HttpServletResponse response,
      final String username,
      final String password);

  void send2FactorAuthOtp(final String contactId);

  void validateSecondFactorAuthentication(final String oneTimePassword);

  void authenticateWithSecondFactorAuthentication(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final String oneTimePassword);

  void logout(final HttpServletRequest request, final HttpServletResponse response);
}
