package br.com.arenco.arenco_clientes.exceptions;

public class ArencoContratosInternalError extends RuntimeException {
  public ArencoContratosInternalError(final Throwable cause) {
    super(cause);
  }

  public ArencoContratosInternalError(final String message) {
    super(message);
  }
}
