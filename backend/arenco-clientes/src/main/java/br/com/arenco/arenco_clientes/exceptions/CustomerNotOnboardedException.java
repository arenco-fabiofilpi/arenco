package br.com.arenco.arenco_clientes.exceptions;

public class CustomerNotOnboardedException extends Exception {
  public CustomerNotOnboardedException(final String message) {
    super(message);
  }
}
