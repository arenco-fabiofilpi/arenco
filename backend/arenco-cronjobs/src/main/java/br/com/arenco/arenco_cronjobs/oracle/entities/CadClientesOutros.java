package br.com.arenco.arenco_cronjobs.oracle.entities;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CadClientesSocioId.class)
@Table(name = "CAD_CLIENTES_OUTROS")
public class CadClientesOutros {
  @Id
  @Column(name = "CLIENTE")
  private Integer cliente;

  @Column(name = "CLIENTE_NASCIMENTO")
  private Date clienteNascimento;

  @Column(name = "CLIENTE_PROFISSAO")
  private String clienteProfissao;

  @Column(name = "CONJUGE_NOME")
  private String conjugeNome;

  @Column(name = "CONJUGE_NASCIMENTO")
  private Date conjugeNascimento;

  @Column(name = "CONJUGE_CIC_NUME")
  private Integer conjugeCicNume;

  @Column(name = "CONJUGE_CIC_DAC")
  private Integer conjugeCicDac;

  @Column(name = "CONJUGE_RG_NUME")
  private String conjugeRgNume;

  @Column(name = "CONJUGE_RG_EMISSAO")
  private String conjugeRgEmissao;

  @Column(name = "CONJUGE_RG_UF")
  private String conjugeRgUf;

  @Column(name = "CONJUGE_PROFISSAO")
  private String conjugeProfissao;

  @Column(name = "REGIME_CASAMENTO")
  private String regimeCasamento;

  @Column(name = "NACIONALIDADE_CLIENTE")
  private String nacionalidadeCliente;

  @Column(name = "CODIGO_REGIME_CASAMENTO")
  private Integer codigoRegimeCasamento;

  @Column(name = "CODIGO_ESTADO_CIVIL")
  private Integer codigoEstadoCivil;

  @Column(name = "RG_CLIENTE")
  private String rgCliente;

  @Column(name = "CLIENTE_SEXO", nullable = false)
  private Integer clienteSexo;

  @Column(name = "CLIENTE_NOME_MAE")
  private String clienteNomeMae;

  @Column(name = "CLIENTE_NOME_PAI")
  private String clienteNomePai;

  @Column(name = "CLIENTE_NATURALIDADE")
  private String clienteNaturalidade;
}
