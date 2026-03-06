package br.com.arenco.arenco_clientes.exceptions;

public class UserIdNotFoundException extends RuntimeException {
  public UserIdNotFoundException(String message) {
    super(message);
  }
}
