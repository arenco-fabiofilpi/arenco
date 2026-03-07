package br.com.arenco.arenco_cronjobs.oracle.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadClientesRefBancariasId {
  private Integer cliente;
  private String banco;
  private String agencia;
}
