package br.com.arenco.arenco_clientes.exceptions;

public class OtpExceededTriesException extends IllegalStateException {
  public OtpExceededTriesException(final String s) {
    super(s);
  }
}
