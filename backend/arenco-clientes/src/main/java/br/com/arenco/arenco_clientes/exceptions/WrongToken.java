package br.com.arenco.arenco_clientes.exceptions;

import lombok.Getter;

@Getter
public class WrongToken extends RuntimeException {
  private final boolean otpTokenValid;

  public WrongToken(final int chancesLeft, boolean otpTokenValid) {
    super(String.format("Token Inválido. Restam %s tentativas", chancesLeft));
    this.otpTokenValid = otpTokenValid;
  }
}
