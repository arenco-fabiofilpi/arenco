package br.com.arenco.arenco_clientes.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGroupDto {
  private String id;
  private String name;
  private String code;
  private int qty;
}
