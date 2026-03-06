package br.com.arenco.arenco_cronjobs.oracle.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
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
@Table(name = "CAD_CLIENTES")
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

  @Column(name = "ENDERECO_COBRANCA")
  private String enderecoCobranca;

  @Column(name = "NUMERO_COBRANCA")
  private String numeroCobranca;

  @Column(name = "CIDADE_COBRANCA")
  private String cidadeCobranca;

  @Column(name = "BAIRRO_COBRANCA")
  private String bairroCobranca;

  @Column(name = "CEP1_COBRANCA")
  private String cep1Cobranca;

  @Column(name = "CEP2_COBRANCA")
  private String cep2Cobranca;

  @Column(name = "ESTADO_COBRANCA")
  private String estadoCobranca;

  @Column(name = "DATA_NASCIMENTO")
  private Date dataNascimento;

  @Column(name = "PROFISSAO")
  private String profissao;

  @Column(name = "CARGO_FUNCAO")
  private String cargoFuncao;

  @Column(name = "PARTICIPACAO")
  private BigDecimal participacao;

  @Column(name = "ASSINA")
  private String assina;

  @Column(name = "TIPO_PROCURACAO")
  private String tipoProcuracao;

  @Column(name = "PARECER_VENDEDOR")
  private String parecerVendedor;

  @Column(name = "CADASTRANTE")
  private String cadastrante;

  @Column(name = "NUMERO_JCOML")
  private String numeroJcoml;

  @Column(name = "NUMERO_ALT_JCOML")
  private String numeroAltJcoml;

  @Column(name = "DATA_REGISTRO_JCOML")
  private Date dataRegistroJcoml;

  @Column(name = "DATA_ALTERACAO_JCOML")
  private Date dataAlteracaoJcoml;

  @Column(name = "CAPITAL_SOCIAL")
  private BigDecimal capitalSocial;

  @Column(name = "DATA_ALTERACAO")
  private Date dataAlteracao;

  @Column(name = "DATA_CADASTRAMENTO")
  private Date dataCadastramento;

  @Column(name = "FAX_COBRANCA")
  private String faxCobranca;

  @Column(name = "FONE_COBRANCA")
  private String foneCobranca;

  @Column(name = "CAIXA_POSTAL_COBRANCA")
  private String caixaPostalCobranca;

  @Column(name = "DDD_COBRANCA")
  private String dddCobranca;

  @Column(name = "MATRIZ")
  private String matriz;

  @Column(name = "CAIXA_POSTAL")
  private String caixaPostal;

  @Column(name = "NOME_FANTASIA")
  private String nomeFantasia;

  @Column(name = "COMPLENDE")
  private String complende;

  @Column(name = "UTILIZA_CEP_COMO_PADRAO")
  private String utilizaCepComoPadrao;

  @Column(name = "FORMULA_CREDITO")
  private String formulaCredito;

  @Column(name = "ENDERECO_ENTREGA")
  private String enderecoEntrega;

  @Column(name = "NUMERO_ENTREGA")
  private String numeroEntrega;

  @Column(name = "CIDADE_ENTREGA")
  private String cidadeEntrega;

  @Column(name = "BAIRRO_ENTREGA")
  private String bairroEntrega;

  @Column(name = "CEP1_ENTREGA")
  private String cep1Entrega;

  @Column(name = "CEP2_ENTREGA")
  private String cep2Entrega;

  @Column(name = "ESTADO_ENTREGA")
  private String estadoEntrega;

  @Column(name = "CAIXA_POSTAL_ENTREGA")
  private String caixaPostalEntrega;

  @Column(name = "DDD_ENTREGA")
  private String dddEntrega;

  @Column(name = "FONE_ENTREGA")
  private String foneEntrega;

  @Column(name = "FAX_ENTREGA")
  private String faxEntrega;

  @Column(name = "COMPLENDE_ENTREGA")
  private String complendeEntrega;

  @Column(name = "COMPLENDE_COBRANCA")
  private String complendeCobranca;

  @Column(name = "CNAE_ATIVIDADE")
  private String cnaeAtividade;

  @Column(name = "CNAE_DACATIV")
  private String cnaeDacativ;

  @Column(name = "CNAE_FISCAL_ATIV")
  private String cnaeFiscalAtiv;

  @Column(name = "DATA_EXPEDICAO_RG")
  private Date dataExpedicaoRg;

  @Column(name = "UTILIZA_GERA_PARCELAS_RECEBER")
  private String utilizaGeraParcelasReceber;

  @Column(name = "NASCIMENTO")
  private Date nascimento;

  @Column(name = "FONE4")
  private String fone4;

  @Column(name = "FONE3")
  private String fone3;

  @Column(name = "CELULAR1")
  private String celular1;

  @Column(name = "RG_DAC_CONJUGE")
  private String rgDacConjuge;

  @Column(name = "EMAIL_COMERCIAL")
  private String emailComercial;

  @Column(name = "RG_NUMERO_CONJUGE")
  private String rgNumeroConjuge;

  @Column(name = "DAC_CPF_CONJUGE")
  private String dacCpfConjuge;

  @Column(name = "NOME_CONJUGE")
  private String nomeConjuge;

  @Column(name = "NUMERO_CPF_CONJUGE")
  private String numeroCpfConjuge;

  @Column(name = "CELULAR2")
  private String celular2;

  @Column(name = "CELULAR3")
  private String celular3;

  @Column(name = "CONSUMIDOR_FINAL")
  private String consumidorFinal;

  @Column(name = "CODIGO_MUNICIPIO")
  private String codigoMunicipio;
}
