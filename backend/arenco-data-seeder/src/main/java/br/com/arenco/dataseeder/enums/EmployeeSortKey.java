package br.com.arenco.dataseeder.enums;

import lombok.Getter;

@Getter
public enum EmployeeSortKey {
  UUID("id"),
  NAME("name"),
  USERNAME("username"),
  ENABLED("enabled"),
  CREATED_AT("createdAt"),
  UPDATED_AT("updatedAt");

  private final String name;

  EmployeeSortKey(final String name) {
    this.name = name;
  }
}
