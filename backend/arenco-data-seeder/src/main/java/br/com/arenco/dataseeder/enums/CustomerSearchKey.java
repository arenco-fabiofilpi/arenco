package br.com.arenco.dataseeder.enums;

import lombok.Getter;

@Getter
public enum CustomerSearchKey {
  UUID("uuid"),
  NAME("name"),
  CONTACT_PHONE("contactPhone"),
  EMAIL("email"),
  USERNAME("username"),
  ENABLED("enabled"),
  GROUPS("groups"),
  CREATED_AT("createdAt"),
  UPDATED_AT("updatedAt"),
  CPF("cpf"),
  CNPJ("cnpj"),
  ID_ERP("idErp");

  private final String name;

  CustomerSearchKey(final String name) {
    this.name = name;
  }
}
