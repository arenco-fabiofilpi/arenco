package br.com.arenco.arenco_clientes.facades;

public interface ResetPasswordFacade {
  void askPasswordReset(final String username);

  void validateResetPasswordToken(final String username, final String token);

  void resetPassword(final String username, final String token, final String password);
}
