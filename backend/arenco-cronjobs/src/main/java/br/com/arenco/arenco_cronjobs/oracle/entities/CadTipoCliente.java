package br.com.arenco.arenco_cronjobs.oracle.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class CadTipoCliente {
  @Id
  @Column(name = "CLIENTE")
  private Integer cliente;

  @Column(name = "TIPO")
  private Integer tipo;
}
