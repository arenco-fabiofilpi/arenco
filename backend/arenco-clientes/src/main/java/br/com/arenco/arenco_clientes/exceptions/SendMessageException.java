package br.com.arenco.arenco_clientes.exceptions;

public class SendMessageException extends RuntimeException {
  public SendMessageException(final String message) {
    super(message);
  }

  public SendMessageException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
