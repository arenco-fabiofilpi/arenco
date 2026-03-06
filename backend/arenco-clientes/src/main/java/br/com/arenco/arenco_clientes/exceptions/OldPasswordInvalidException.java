package br.com.arenco.arenco_clientes.exceptions;

public class OldPasswordInvalidException extends RuntimeException {
  public OldPasswordInvalidException(final Throwable cause) {
    super(cause);
  }
}
