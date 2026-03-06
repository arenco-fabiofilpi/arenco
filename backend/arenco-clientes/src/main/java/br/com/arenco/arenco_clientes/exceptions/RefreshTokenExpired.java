package br.com.arenco.arenco_clientes.exceptions;

public class RefreshTokenExpired extends RuntimeException {
  public RefreshTokenExpired(final String message) {
    super(message);
  }
}
