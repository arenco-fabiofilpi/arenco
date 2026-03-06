package br.com.arenco.arenco_clientes.exceptions;

import org.springframework.security.core.AuthenticationException;

public class CustomerBlockedException extends AuthenticationException {
  public CustomerBlockedException() {
    super("Cliente Bloqueado");
  }
}
