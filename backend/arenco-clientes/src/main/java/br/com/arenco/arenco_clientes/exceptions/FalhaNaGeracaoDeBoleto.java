package br.com.arenco.arenco_clientes.exceptions;

public class FalhaNaGeracaoDeBoleto extends RuntimeException {
  public FalhaNaGeracaoDeBoleto(final String message, final Throwable cause) {
    super(message, cause);
  }
}
