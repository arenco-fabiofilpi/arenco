package br.com.arenco.arenco_clientes.exceptions;

public class MessengerStrategyNotFound extends RuntimeException {
  public MessengerStrategyNotFound(String message) {
    super(message);
  }
}
