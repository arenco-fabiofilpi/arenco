package br.com.arenco.arenco_cronjobs.oracle.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TituloRecebidoIdOracle.class)
@Table(name = "cr_titulos_rec_ccusto")
public class TituloRecebidoOracle {
  @Id
  @Column(name = "EMPRESA")
  private String empresa;

  @Id
  @Column(name = "CLIENTE")
  private String cliente;

  @Id
  @Column(name = "NUME_DOC")
  private String numeDoc;

  @Id
  @Column(name = "SEQUENCIA")
  private String sequencia;

  @Id
  @Column(name = "LOTE")
  private String lote;

  @Column(name = "TIPO_DOC")
  private String tipoDoc;

  @Column(name = "DESCRICAO")
  private String descricao;

  @Column(name = "SERIE")
  private String serie;

  @Column(name = "DATA_BASE")
  private String dataBase;

  @Column(name = "DT_EMISSAO")
  private String dtEmissao;

  @Column(name = "DT_DEPOSITO")
  private String dtDeposito;

  @Column(name = "VENCIMENTO")
  private String vencimento;

  @Column(name = "MOEDA")
  private String moeda;

  @Column(name = "VALOR")
  private String valor;

  @Column(name = "VALOR_JUROS")
  private String valorJuros;

  @Column(name = "VALOR_DESC")
  private String valorDesc;

  @Column(name = "VALOR_PAGO")
  private String valorPago;

  @Column(name = "VALOR_VM")
  private String valorVm;

  @Column(name = "VALOR_MULTA")
  private String valorMulta;

  @Column(name = "OBSERVACAO")
  private String observacao;

  @Column(name = "NOME_CLTE")
  private String nomeClte;

  @Column(name = "FATURA")
  private String fatura;

  @Column(name = "NUME_FATURA")
  private String numeFatura;

  @Column(name = "NUME_DEPOSITO")
  private String numeDeposito;

  @Column(name = "DENOMINACAO")
  private String denominacao;

  @Column(name = "NOME_CCUSTO")
  private String nomeCcusto;

  @Column(name = "CCUSTO")
  private String ccusto;

  @Column(name = "RECEITA")
  private String receita;

  @Column(name = "BANCO")
  private String banco;

  @Column(name = "VALOR_DEPOSITO")
  private String valorDeposito;

  @Column(name = "CODIGO_BANCO")
  private String codigoBanco;

  @Column(name = "AGENCIA")
  private String agencia;

  @Column(name = "CONTA")
  private String conta;

  @Column(name = "NOME_BANCO")
  private String nomeBanco;

  @Column(name = "TIPO_BAIXA")
  private String tipoBaixa;

  @Column(name = "DESCRICAO_TIPO_BAIXA")
  private String descricaoTipoBaixa;

  @Column(name = "DIAS_ATRASO")
  private String diasAtraso;

  @Column(name = "QUADRA")
  private String quadra;

  @Column(name = "LOTE_LOTEAMENTO")
  private String loteLoteamento;

  @Column(name = "UNIDADE_DE_CUSTO")
  private String unidadeDeCusto;

  @Column(name = "DATA_GRAVACAO")
  private String dataGravacao;

  @Column(name = "QUADRA_ORIGINAL")
  private String quadraOriginal;

  @Column(name = "LOTE_ORIGINAL")
  private String loteOriginal;

  @Column(name = "UNIDADE_DE_CUSTO_ORIGINAL")
  private String unidadeDeCustoOriginal;

  @Column(name = "VALOR_SEGURO")
  private String valorSeguro;

  @Column(name = "TIPO_DEPOSITO")
  private String tipoDeposito;

  @Column(name = "DESCRICAO_TIPO_DEPOSITO")
  private String descricaoTipoDeposito;

  @Column(name = "SEQUENCIA_REPARCELA")
  private String sequenciaReparcela;

  @Column(name = "TOTAL_REPARCELA")
  private String totalReparcela;

  @Column(name = "DATA_REPARCELA")
  private String dataReparcela;

  @Column(name = "OBSERVACAO_CONTRATO")
  private String observacaoContrato;
}
