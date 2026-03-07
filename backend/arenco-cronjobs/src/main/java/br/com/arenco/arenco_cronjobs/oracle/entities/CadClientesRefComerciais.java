package br.com.arenco.arenco_cronjobs.oracle.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CadClientesRefComerciaisId.class)
@Table(name = "CAD_CLIENTES_REF_COMERCIAIS")
public class CadClientesRefComerciais {
  @Id
  @Column(name = "CLIENTE")
  private Integer cliente;

  @Id
  @Column(name = "NOME")
  private String nome;

  @Column(name = "ENDERECO")
  private String endereco;

  @Column(name = "NUMERO")
  private Integer numero;

  @Column(name = "BAIRRO")
  private String bairro;

  @Column(name = "CIDADE")
  private String cidade;

  @Column(name = "CEP1")
  private Integer cep1;

  @Column(name = "CEP2")
  private Integer cep2;

  @Column(name = "ESTADO")
  private String estado;

  @Column(name = "DDD")
  private Integer ddd;

  @Column(name = "FONE1")
  private Integer fone1;

  @Column(name = "FONE2")
  private Integer fone2;
}
