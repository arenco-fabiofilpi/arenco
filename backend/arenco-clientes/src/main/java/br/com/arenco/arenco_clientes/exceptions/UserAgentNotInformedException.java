package br.com.arenco.arenco_clientes.exceptions;

public class UserAgentNotInformedException extends RuntimeException {
  public UserAgentNotInformedException(String message) {
    super(message);
  }
}
