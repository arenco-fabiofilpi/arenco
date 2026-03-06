package br.com.arenco.arenco_clientes.exceptions;

import org.springframework.security.core.AuthenticationException;

public class CustomerDisabledException extends AuthenticationException {
  public CustomerDisabledException() {
    super("Cliente Desabilitado");
  }
}
