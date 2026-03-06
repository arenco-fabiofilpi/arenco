package br.com.arenco.arenco_clientes.enums;

import lombok.Getter;

@Getter
public enum LoginMethod {
  USERNAME_ONLY("Apenas nome de Usuário"),
  TWO_FACTOR_AUTHENTICATION("2FA"),
  USERNAME_AND_PASSWORD("Usuário e Senha");

  private final String name;

  LoginMethod(final String name) {
    this.name = name;
  }
}
