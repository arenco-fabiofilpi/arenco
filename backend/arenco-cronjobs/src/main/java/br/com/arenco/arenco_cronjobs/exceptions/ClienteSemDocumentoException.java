package br.com.arenco.arenco_cronjobs.exceptions;

public class ClienteSemDocumentoException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public ClienteSemDocumentoException(final String message) {
    super(message);
  }
}
