package br.com.arenco.arenco_clientes.exceptions;

public class MultipleSessionsNotHandled extends RuntimeException {
  public MultipleSessionsNotHandled(final String message) {
    super(message);
  }
}
