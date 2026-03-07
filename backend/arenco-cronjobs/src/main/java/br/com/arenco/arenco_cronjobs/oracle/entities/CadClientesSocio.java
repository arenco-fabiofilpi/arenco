package br.com.arenco.arenco_cronjobs.oracle.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CadClientesSocioId.class)
@Table(name = "CAD_CLIENTES_SOCIOS")
public class CadClientesSocio {
  @Id
  @Column(name = "CLIENTE")
  private Integer cliente;

  @Column(name = "NOME")
  private String nome;

  @Column(name = "DDD")
  private Integer ddd;

  @Column(name = "FONE1")
  private Integer fone1;

  @Column(name = "FONE2")
  private Integer fone2;

  @Id
  @Column(name = "CIC_NUME")
  private Integer cicNume;

  @Id
  @Column(name = "CIC_DAC")
  private Integer cicDac;

  @Id
  @Column(name = "RG_NUME")
  private Integer rgNume;

  @Column(name = "RG_EMISSAO")
  private String rgEmissao;

  @Column(name = "RG_UF")
  private String rgUf;

  @Column(name = "DATA_NASCIMENTO")
  private Date dataNascimento;

  @Column(name = "PROFISSAO")
  private String profissao;

  @Column(name = "CARGO_FUNCAO")
  private String cargoFuncao;

  @Column(name = "PARTICIPACAO")
  private BigDecimal participacao;

  @Column(name = "ESTADO")
  private String estado;

  @Column(name = "ASSINA")
  private Integer assina;

  @Column(name = "TIPO_PROCURACAO")
  private Integer tipoProcuracao;

  @Column(name = "CIDADE")
  private String cidade;

  @Column(name = "CEP1")
  private Integer cep1;

  @Column(name = "CEP2")
  private Integer cep2;

  @Column(name = "BAIRRO")
  private String bairro;

  @Column(name = "NUMERO")
  private Integer numero;

  @Column(name = "ENDERECO")
  private String endereco;

  @Column(name = "NOME_FANTASIA")
  private String nomeFantasia;

  @Column(name = "CODIGO_REGIME_CASAMENTO")
  private Integer codigoRegimeCasamento;

  @Column(name = "CODIGO_ESTADO_CIVIL")
  private Integer codigoEstadoCivil;

  @Column(name = "NACIONALIDADE_SOCIO")
  private String nacionalidadeSocio;
}
