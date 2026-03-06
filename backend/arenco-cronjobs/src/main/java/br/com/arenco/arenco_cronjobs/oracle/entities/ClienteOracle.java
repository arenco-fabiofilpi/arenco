package br.com.arenco.arenco_cronjobs.oracle.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
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
@Table(name = "cad_consulta_clientes")
public class ClienteOracle implements Serializable {
  @Id
  @Column(name = "CODIGO")
  private String codigo;

  @Column(name = "NOME")
  private String nome;

  @Column(name = "ENDERECO")
  private String endereco;

  @Column(name = "NUMERO")
  private String numero;

  @Column(name = "BAIRRO")
  private String bairro;

  @Column(name = "CIDADE")
  private String cidade;

  @Column(name = "CEP1")
  private String cep1;

  @Column(name = "CEP2")
  private String cep2;

  @Column(name = "CEP")
  private String cep;

  @Column(name = "ESTADO")
  private String estado;

  @Column(name = "PAIS")
  private String pais;

  @Column(name = "DDD")
  private String ddd;

  @Column(name = "FONE1")
  private String fone1;

  @Column(name = "FONE2")
  private String fone2;

  @Column(name = "FONEFAX")
  private String fonefax;

  @Column(name = "EMAIL_INTERNET")
  private String emailInternet;

  @Column(name = "CGC_NUMERO")
  private String cgcNumero;

  @Column(name = "CGC_FILIAL")
  private String cgcFilial;

  @Column(name = "CGC_DAC")
  private String cgcDac;

  @Column(name = "CIC_NUME")
  private String cicNume;

  @Column(name = "CIC_DAC")
  private String cicDac;

  @Column(name = "RG_NUME")
  private String rgNume;

  @Column(name = "RG_EMISSAO")
  private String rgEmissao;

  @Column(name = "RG_UF")
  private String rgUf;

  @Column(name = "INSCRICAO")
  private String inscricao;

  @Column(name = "INSS")
  private String inss;

  @Column(name = "CONTATO")
  private String contato;

  @Column(name = "GRUPO_CONTABIL")
  private String grupoContabil;

  @Column(name = "WWW_INTERNET")
  private String wwwInternet;

  @Column(name = "EM_USO")
  private String emUso;

  @Column(name = "OBSERVACAO")
  private String observacao;

  @Column(name = "CATEGORIA")
  private String categoria;

  @Column(name = "VENDEDOR")
  private String vendedor;

  @Column(name = "NOME_VENDEDOR")
  private String nomeVendedor;
}
