package br.com.arenco.dataseeder.entities;

import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@ToString
public class ReceivedTitleModel {

  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;

  private String contratoId;

  private String empresa;
  private String cliente;
  private String numeDoc;
  private String sequencia;
  private String lote;
  private String tipoDoc;
  private String descricao;
  private String serie;
  private LocalDate dataBase;
  private LocalDate dtEmissao;
  private LocalDate dtDeposito;
  private LocalDate vencimento;
  private String moeda;
  private Double valor;
  private Double valorJuros;
  private Double valorDesc;
  private Double valorPago;
  private Double valorVm;
  private Double valorMulta;
  private String observacao;
  private String nomeClte;
  private String fatura;
  private String numeFatura;
  private String numeDeposito;
  private String denominacao;
  private String nomeCcusto;
  private String ccusto;
  private String receita;
  private String banco;
  private Double valorDeposito;
  private String codigoBanco;
  private String agencia;
  private String conta;
  private String nomeBanco;
  private String tipoBaixa;
  private String descricaoTipoBaixa;
  private String diasAtraso;
  private String quadra;
  private String loteLoteamento;
  private String unidadeDeCusto;
  private String dataGravacao;
  private String quadraOriginal;
  private String loteOriginal;
  private String unidadeDeCustoOriginal;
  private Double valorSeguro;
  private String tipoDeposito;
  private String descricaoTipoDeposito;
  private String sequenciaReparcela;
  private Double totalReparcela;
  private String dataReparcela;
  private String observacaoContrato;
}
