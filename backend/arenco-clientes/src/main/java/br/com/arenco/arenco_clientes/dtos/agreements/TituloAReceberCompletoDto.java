package br.com.arenco.arenco_clientes.dtos.agreements;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TituloAReceberCompletoDto {
  private String id;
  private String empresa;
  private int cliente;
  private String nomeClte;
  private String numeDoc;
  private String sequencia;
  private String lote;
  private String dtEmissao;
  private String dataBase;
  private String ccusto;
  private String nomeCcusto;
  private String fatura;
  private String numeFatura;
  private String vencimento;
  private String valor;
  private String tipoDoc;
  private String serie;
  private String observacao;
  private String saldo;
  private String unidadeDeCusto;
  private String descricao;
  private String receita;
  private String denominacao;
  private String dataDeHoje;
  private String moeda;
  private String descricaoMoeda;
  private Double qtdeMoeda;
  private Double saldoQtdeMoeda;
  private String valorCorrigidoVencimento;
  private String valorCorrigidoHoje;
  private String valorCorrigidoDataRef;
  private String codigoPortador;
  private String temCobrancaBoleto;
  private String quadra;
  private String indicacaoSituacaoJuridica;
  private String parcelasLiberadaApos;
  private String tipoCorrecaoContrato;
  private String taxaJurosValorPresente;
  private String saldoValorPresente;
  private String seguroEmbutidoParcela;
  private String percentualSeguro;
  private String codigoCobranca;
  private String cnpjCpfDoCliente;
  private String sequenciaReparcela;
  private String dataReparcela;
  private String totalReparcela;
  private String pdfId;
  private String pngId;
}
