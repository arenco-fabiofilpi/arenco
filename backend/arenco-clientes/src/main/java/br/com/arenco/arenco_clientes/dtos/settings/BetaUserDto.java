package br.com.arenco.arenco_clientes.dtos.settings;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BetaUserDto {
  private String id;
  private String createdAt;
  private String updatedAt;
  private String name;
  private int idErp;
  private String username;
}
