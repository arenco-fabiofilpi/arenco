package br.com.arenco.arenco_clientes.exceptions;

public class MultiplePasswordRecoveryTokenRequestException extends RuntimeException {
  public MultiplePasswordRecoveryTokenRequestException(String message) {
    super(message);
  }
}
