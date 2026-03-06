package br.com.arenco.arenco_clientes.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidSessionException extends AuthenticationException {
  public InvalidSessionException() {
    super("Sessão Inválida");
  }
}
