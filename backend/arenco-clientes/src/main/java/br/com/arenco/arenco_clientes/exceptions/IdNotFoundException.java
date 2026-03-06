package br.com.arenco.arenco_clientes.exceptions;

public class IdNotFoundException extends RuntimeException {
  public IdNotFoundException(String message) {
    super(message);
  }
}
