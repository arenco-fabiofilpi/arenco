package br.com.arenco.arenco_cronjobs.oracle.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
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
@IdClass(TituloAReceberIdOracle.class)
@Table(name = "cr_titulos_a_receber_ccusto")
public class TituloAReceberOracle implements Serializable {

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

  @Column(name = "DT_EMISSAO")
  private String dtEmissao;

  @Column(name = "DATA_BASE")
  private String dataBase;

  @Column(name = "FATURA")
  private String fatura;

  @Column(name = "NOME_CLTE")
  private String nomeClte;

  @Column(name = "NUME_FATURA")
  private String numeFatura;

  @Column(name = "OBSERVACAO")
  private String observacao;

  @Column(name = "SALDO")
  private String saldo;

  @Column(name = "SERIE")
  private String serie;

  @Column(name = "UNIDADE_DE_CUSTO")
  private String unidadeDeCusto;

  @Column(name = "TIPO_DOC")
  private String tipoDoc;

  @Column(name = "VALOR")
  private String valor;

  @Column(name = "VENCIMENTO")
  private String vencimento;

  @Column(name = "DESCRICAO")
  private String descricao;

  @Column(name = "NOME_CCUSTO")
  private String nomeCcusto;

  @Column(name = "CCUSTO")
  private String ccusto;

  @Column(name = "RECEITA")
  private String receita;

  @Column(name = "DENOMINACAO")
  private String denominacao;

  @Column(name = "DATA_DE_HOJE")
  private String dataDeHoje;

  @Column(name = "MOEDA")
  private String moeda;

  @Column(name = "DESCRICAO_MOEDA")
  private String descricaoMoeda;

  @Column(name = "QTDE_MOEDA")
  private String qtdeMoeda;

  @Column(name = "SALDO_QTDE_MOEDA")
  private String saldoQtdeMoeda;

  @Column(name = "VALOR_CORRIGIDO_VENCIMENTO")
  private String valorCorrigidoVencimento;

  @Column(name = "VALOR_CORRIGIDO_HOJE")
  private String valorCorrigidoHoje;

  @Column(name = "VALOR_CORRIGIDO_DATA_REF")
  private String valorCorrigidoDataRef;

  @Column(name = "CODIGO_PORTADOR")
  private String codigoPortador;

  @Column(name = "TEM_COBRANCA_BOLETO")
  private String temCobrancaBoleto;

  @Column(name = "QUADRA")
  private String quadra;

  @Column(name = "LOTE")
  private String lote;

  @Column(name = "INDICACAO_SITUACAO_JURIDICA")
  private String indicacaoSituacaoJuridica;

  @Column(name = "PARCELAS_LIBERADA_APOS")
  private String parcelasLiberadaApos;

  @Column(name = "TIPO_CORRECAO_CONTRATO")
  private String tipoCorrecaoContrato;

  @Column(name = "TAXA_JUROS_VALOR_PRESENTE")
  private String taxaJurosValorPresente;

  @Column(name = "SALDO_VALOR_PRESENTE")
  private String saldoValorPresente;

  @Column(name = "SEGURO_EMBUTIDO_PARCELA")
  private String seguroEmbutidoParcela;

  @Column(name = "PERCENTUAL_SEGURO")
  private String percentualSeguro;

  @Column(name = "CODIGO_COBRANCA")
  private String codigoCobranca;

  @Column(name = "CNPJ_CPF_DO_CLIENTE")
  private String cnpjCpfDoCliente;

  @Column(name = "SEQUENCIA_REPARCELA")
  private String sequenciaReparcela;

  @Column(name = "DATA_REPARCELA")
  private String dataReparcela;

  @Column(name = "TOTAL_REPARCELA")
  private String totalReparcela;
}
