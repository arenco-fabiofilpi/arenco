package br.com.arenco.arenco_clientes.exceptions;

public class WrongPasswordRecoveryTokenException extends RuntimeException {
  public WrongPasswordRecoveryTokenException(final String message) {
    super(message);
  }
}
