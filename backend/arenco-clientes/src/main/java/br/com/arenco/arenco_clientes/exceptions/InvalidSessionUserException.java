package br.com.arenco.arenco_clientes.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidSessionUserException extends AuthenticationException {
  public InvalidSessionUserException(final String sessionUserId) {
    super(String.format("Usuário %s não encontrado", sessionUserId));
  }
}
