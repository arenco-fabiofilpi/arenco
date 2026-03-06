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
@IdClass(ContratoIdOracle.class)
@Table(name = "cr_consulta_contrato")
public class ContratoOracle {
  @Id
  @Column(name = "EMPRESA")
  private String empresa;

  @Id
  @Column(name = "NUME_CONTRATO")
  private String numeContrato;

  @Column(name = "NOME_EMPRESA")
  private String nomeEmpresa;

  @Column(name = "CLIENTE")
  private String cliente;

  @Column(name = "NOME_CLIENTE")
  private String nomeCliente;

  @Column(name = "CCUSTO")
  private String ccusto;

  @Column(name = "NOME_CCUSTO")
  private String nomeCcusto;

  @Column(name = "ENDERECO_CCUSTO")
  private String enderecoCcusto;

  @Column(name = "UNIDADE_DE_CUSTO")
  private String unidadeDeCusto;

  @Column(name = "RECEITA")
  private String receita;

  @Column(name = "DENOMINACAO")
  private String denominacao;

  @Column(name = "DT_EMISSAO")
  private String dtEmissao;

  @Column(name = "DATA_BASE")
  private String dataBase;

  @Column(name = "SEQUENCIA_INICIAL")
  private String sequenciaInicial;

  @Column(name = "VALOR_CONTRATO")
  private String valorContrato;

  @Column(name = "VALOR_CONTRATO_EXTENSO")
  private String valorContratoExtenso;

  @Column(name = "NUME_CONTRATO_ANTERIOR")
  private String numeContratoAnterior;

  @Column(name = "TEM_LANCTO_CONTAB")
  private String temLanctoContab;

  @Column(name = "TIPO_DOC")
  private String tipoDoc;

  @Column(name = "DESCRICAO")
  private String descricao;

  @Column(name = "OBSERVACAO_CONTRATO")
  private String observacaoContrato;

  @Column(name = "NUME_RENEGOCIACAO")
  private String numeRenegociacao;

  @Column(name = "DATA_RENEGOCIACAO")
  private String dataRenegociacao;

  @Column(name = "OBSERVACAO_RENEGOCIACAO")
  private String observacaoRenegociacao;

  @Column(name = "DIFERENCA_RENEGOCIACAO")
  private String diferencaRenegociacao;

  @Column(name = "DATA_RESCISAO")
  private String dataRescisao;

  @Column(name = "OBSERVACAO_RESCISAO")
  private String observacaoRescisao;

  @Column(name = "JUROS1")
  private String juros1;

  @Column(name = "DATA_JUROS1")
  private String dataJuros1;

  @Column(name = "JUROS2")
  private String juros2;

  @Column(name = "DATA_JUROS2")
  private String dataJuros2;

  @Column(name = "JUROS3")
  private String juros3;

  @Column(name = "DATA_JUROS3")
  private String dataJuros3;

  @Column(name = "CIC")
  private String cic;

  @Column(name = "CGC_CLIENTE")
  private String cgcCliente;

  @Column(name = "INSCRICAO_ESTADUAL_CLIENTE")
  private String inscricaoEstadualCliente;

  @Column(name = "ENDERECO_COMPLETO")
  private String enderecoCompleto;

  @Column(name = "CIDADE")
  private String cidade;

  @Column(name = "ESTADO")
  private String estado;

  @Column(name = "CEP_COMPLETO")
  private String cepCompleto;

  @Column(name = "VENDEDOR")
  private String vendedor;

  @Column(name = "NOME_VENDEDOR")
  private String nomeVendedor;

  @Column(name = "TIPO_CCUSTO")
  private String tipoCcusto;

  @Column(name = "QUADRA")
  private String quadra;

  @Column(name = "LOTE")
  private String lote;

  @Column(name = "INDICACAO_SITUACAO_JURIDICA")
  private String indicacaoSituacaoJuridica;

  @Column(name = "PARCELAS_LIBERADA_APOS")
  private String parcelasLiberadaApos;

  @Column(name = "CIDADE_EMPRESA")
  private String cidadeEmpresa;

  @Column(name = "CNPJ_EMPRESA")
  private String cnpjEmpresa;

  @Column(name = "ENDERECO_COMPLETO_EMPRESA")
  private String enderecoCompletoEmpresa;

  @Column(name = "BAIRRO")
  private String bairro;

  @Column(name = "METRAGEM")
  private String metragem;

  @Column(name = "TIPO_CORRECAO_CONTRATO")
  private String tipoCorrecaoContrato;

  @Column(name = "CPF_CONJUGE")
  private String cpfConjugue;

  @Column(name = "CODIGO_NOME_CLIENTE")
  private String codigoNomeCliente;

  @Column(name = "ENDERECO_COMPLETO_CLIENTE")
  private String enderecoCompletoCliente;

  @Column(name = "TELEFONES")
  private String telefones;

  @Column(name = "UNIDADE_QUADRA_LOTE")
  private String unidadeQuadraLote;

  @Column(name = "TIPO_UNIDADE")
  private String tipoUnidade;

  @Column(name = "CONJUGE_NOME")
  private String conjugueNome;

  @Column(name = "USUARIO_INCLUSAO")
  private String usuarioInclusao;

  @Column(name = "DATA_INCLUSAO")
  private String dataInclusao;

  @Column(name = "PERCENTUAL_UA_CSN")
  private String percentualUaCsn;

  @Column(name = "NUMERO_CONTRATO_INTERNO")
  private String numeroContratoInterno;

  @Column(name = "CLIENTE_NASCIMENTO")
  private String clienteNascimento;

  @Column(name = "RG_CLIENTE")
  private String rgCliente;

  @Column(name = "PERCENTUAL_SEGURO")
  private String percentualSeguro;

  @Column(name = "SEGURO_EMBUTIDO_PARCELA")
  private String seguroEmbutidoParcela;

  @Column(name = "TAXA_JUROS_VALOR_PRESENTE")
  private String taxaJurosValorPresente;

  @Column(name = "NUMERO_CONTRATO_MATRIZ")
  private String numeroContratoMatriz;

  @Column(name = "NUME_CONTRATO_ORIGINAL")
  private String numeContratoOriginal;

  @Column(name = "UNIDADE_DE_CUSTO_ORIGINAL")
  private String unidadeDeCustoOriginal;

  @Column(name = "QUADRA_ORIGINAL")
  private String quadraOriginal;

  @Column(name = "LOTE_ORIGINAL")
  private String loteOriginal;

  @Column(name = "CONTRATO_ANTERIOR_CESSAO")
  private String contratoAnteriorCessao;

  @Column(name = "DATA_ENTREGA_UNIDADE")
  private String dataEntregaUnidade;

  @Column(name = "NOME_CORRETOR")
  private String nomeCorretor;

  @Column(name = "TOTAL_SALDO_EM_ABERTO")
  private String totalSaldoEmAberto;

  @Column(name = "COD_JUROS_COBRANCA_INDIVIDUAL")
  private String codJurosCobrancaIndividual;

  @Column(name = "DESC_JUROS_COBRANCA_INDIVIDUAL")
  private String descJurosCobrancaIndividual;

  @Column(name = "PERC_MULTA_COBRANCA_INDIVIDUAL")
  private String percMultaCobrancaIndividual;

  @Column(name = "SEM_CARENCIA_ARRED_JUROS_MULTA")
  private String semCarenciaArredJurosMulta;

  @Column(name = "NOME_ULTIMO_COMPRADOR")
  private String nomeUltimoComprador;

  @Column(name = "OBSERVACAO_CADASTRO_CLIENTE")
  private String observacaoCadastroCliente;
}
