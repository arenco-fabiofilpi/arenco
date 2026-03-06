package br.com.arenco.arenco_clientes.dtos.sync;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobInfoDto {
  private String id;
  private String dateCreated;
  private String lastUpdated;
  private String type;
  private String status;
  private String startedAt;
  private String finishedAt; // pode ser null enquanto roda
  private String message; // opcional: msg de erro/ok
}
