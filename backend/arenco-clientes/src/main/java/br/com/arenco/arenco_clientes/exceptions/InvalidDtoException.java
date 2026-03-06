package br.com.arenco.arenco_clientes.exceptions;

public class InvalidDtoException extends RuntimeException {
  public InvalidDtoException(final String message) {
    super(message);
  }
}
