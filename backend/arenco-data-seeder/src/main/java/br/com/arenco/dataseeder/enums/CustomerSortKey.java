package br.com.arenco.dataseeder.enums;

import lombok.Getter;

@Getter
public enum CustomerSortKey {
  UUID("id"),
  NAME("name"),
  USERNAME("username"),
  ENABLED("enabled"),
  CREATED_AT("createdAt"),
  UPDATED_AT("updatedAt"),
  CPF("cpf"),
  CNPJ("cnpj"),
  ID_ERP("idErp");

  private final String name;

  CustomerSortKey(final String name) {
    this.name = name;
  }
}
