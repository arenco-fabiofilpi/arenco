package br.com.arenco.arenco_cronjobs.oracle.entities;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoIdOracle implements Serializable {
  private String empresa;
  private String numeContrato;
}
