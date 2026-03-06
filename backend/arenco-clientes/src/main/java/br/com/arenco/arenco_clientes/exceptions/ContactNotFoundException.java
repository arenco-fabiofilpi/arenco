package br.com.arenco.arenco_clientes.exceptions;

public class ContactNotFoundException extends RuntimeException {
  public ContactNotFoundException(String message) {
    super(message);
  }
}
