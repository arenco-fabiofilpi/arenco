package br.com.arenco.arenco_clientes.entities;

import java.time.Instant;
import java.util.Date;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Setter
@Getter
@Document
public class CadClientesSocioModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;

  private int cliente;
  private String nome;
  private String ddd;
  private String fone1;
  private String fone2;
  private String cicNume;
  private String cicDac;
  private String rgNume;
  private String rgEmissao;
  private String rgUf;
  private Date dataNascimento;
  private String profissao;
  private String cargoFuncao;
  private Double participacao;
  private String estado;
  private String assina;
  private String tipoProcuracao;
  private String cidade;
  private String cep1;
  private String cep2;
  private String bairro;
  private String numero;
  private String endereco;
  private String nomeFantasia;
  private String codigoRegimeCasamento;
  private String codigoEstadoCivil;
  private String nacionalidadeSocio;
}
