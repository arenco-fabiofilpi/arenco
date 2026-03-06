package br.com.arenco.dataseeder.enums;

import lombok.Getter;

@Getter
public enum EmployeeSearchKey {
  UUID("uuid"),
  NAME("name"),
  CONTACT_PHONE("contactPhone"),
  EMAIL("email"),
  USERNAME("username"),
  ENABLED("enabled"),
  GROUPS("groups"),
  CREATED_AT("createdAt"),
  UPDATED_AT("updatedAt");

  private final String name;

  EmployeeSearchKey(final String name) {
    this.name = name;
  }
}
