package br.com.arenco.arenco_cronjobs.exceptions;

public class FalhaNoUploadDoBoleto extends RuntimeException {
  public FalhaNoUploadDoBoleto(final String message, final Throwable cause) {
    super(message, cause);
  }
}
