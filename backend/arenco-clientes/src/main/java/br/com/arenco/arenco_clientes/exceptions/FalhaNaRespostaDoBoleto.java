package br.com.arenco.arenco_clientes.exceptions;

public class FalhaNaRespostaDoBoleto extends RuntimeException {
  public FalhaNaRespostaDoBoleto(final String message, final Throwable cause) {
    super(message, cause);
  }
}
