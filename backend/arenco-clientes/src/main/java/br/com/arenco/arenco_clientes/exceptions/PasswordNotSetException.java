package br.com.arenco.arenco_clientes.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PasswordNotSetException extends UsernameNotFoundException {
  public PasswordNotSetException(String msg) {
    super(msg);
  }
}
