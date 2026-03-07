package br.com.arenco.arenco_cronjobs.oracle.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CAD_TIPO_CLTE")
public class CadTipoCliente {
  @Id
  @Column(name = "CLIENTE")
  private Integer cliente;

  @Column(name = "TIPO")
  private Integer tipo;
}
