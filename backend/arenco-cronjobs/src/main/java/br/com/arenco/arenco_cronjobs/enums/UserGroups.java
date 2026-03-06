package br.com.arenco.arenco_cronjobs.enums;

import lombok.Getter;

@Getter
public enum UserGroups {
  ADMIN("administradores"),
  CUSTOMERS("clientes"),
  LEGAL_ENTITIES("clientesPessoaJuridica"),
  NATURAL_PERSONS("clientesPessoaFisica");

  private final String code;

  UserGroups(final String code) {
    this.code = code;
  }
}
