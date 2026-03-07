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
public class CadClientesOutrosModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;
  private Integer cliente;
  private Date clienteNascimento;
  private String clienteProfissao;
  private String conjugeNome;
  private Date conjugeNascimento;
  private Integer conjugeCicNume;
  private Integer conjugeCicDac;
  private String conjugeRgNume;
  private String conjugeRgEmissao;
  private String conjugeRgUf;
  private String conjugeProfissao;
  private String regimeCasamento;
  private String nacionalidadeCliente;
  private Integer codigoRegimeCasamento;
  private Integer codigoEstadoCivil;
  private String rgCliente;
  private Integer clienteSexo;
  private String clienteNomeMae;
  private String clienteNomePai;
  private String clienteNaturalidade;
}
