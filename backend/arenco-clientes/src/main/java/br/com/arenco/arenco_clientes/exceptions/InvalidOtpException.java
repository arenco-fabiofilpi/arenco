package br.com.arenco.arenco_clientes.exceptions;

public class InvalidOtpException extends IllegalStateException {
  public InvalidOtpException(final String s) {
    super(s);
  }
}
