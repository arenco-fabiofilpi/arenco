package br.com.arenco.arenco_clientes.enums;

import lombok.Getter;

@Getter
public enum ContactType {
  CELLPHONE("Celular"),
  TELEPHONE("Telefone"),
  EMAIL("E-mail");

  private final String name;

  ContactType(final String name) {
    this.name = name;
  }
}
