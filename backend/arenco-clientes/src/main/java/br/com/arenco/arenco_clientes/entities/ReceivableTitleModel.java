package br.com.arenco.arenco_clientes.entities;

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
public class ReceivableTitleModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;

  private String contratoId;
  private String pdfId;
  private String pngId;

  private String empresa;
  private int cliente;
  private String numeDoc;
  private String sequencia;
  private LocalDate dtEmissao;
  private LocalDate dataBase;
  private String fatura;
  private String nomeClte;
  private String numeFatura;
  private String observacao;
  private Double saldo;
  private String serie;
  private String unidadeDeCusto;
  private String tipoDoc;
  private Double valor;
  private LocalDate vencimento;
  private String descricao;
  private String nomeCcusto;
  private String ccusto;
  private String receita;
  private String denominacao;
  private LocalDate dataDeHoje;
  private String moeda;
  private String descricaoMoeda;
  private Double qtdeMoeda;
  private Double saldoQtdeMoeda;
  private Double valorCorrigidoVencimento;
  private Double valorCorrigidoHoje;
  private Double valorCorrigidoDataRef;
  private String codigoPortador;
  private String temCobrancaBoleto;
  private String quadra;
  private String lote;
  private String indicacaoSituacaoJuridica;
  private String parcelasLiberadaApos;
  private String tipoCorrecaoContrato;
  private String taxaJurosValorPresente;
  private Double saldoValorPresente;
  private String seguroEmbutidoParcela;
  private String percentualSeguro;
  private String codigoCobranca;
  private String cnpjCpfDoCliente;
  private String sequenciaReparcela;
  private String dataReparcela;
  private String totalReparcela;
}
