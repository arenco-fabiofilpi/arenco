package br.com.arenco.arenco_clientes.exceptions;

public class NoContactFoundException extends RuntimeException {
  public NoContactFoundException(String message) {
    super(message);
  }
}
