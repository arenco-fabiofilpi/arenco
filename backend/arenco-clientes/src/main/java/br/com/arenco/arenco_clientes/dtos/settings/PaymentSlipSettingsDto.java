package br.com.arenco.arenco_clientes.dtos.settings;

import br.com.arenco.arenco_clientes.enums.Bank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSlipSettingsDto {
  private String id;
  private Long version;
  private String dateCreated;
  private String lastUpdated;
  private String localDePagamento;
  private String nomeBeneficiario;
  private String instrucoes;
  private Bank banco;
  private String documento;
  private String agencia;
  private String codigoBeneficiario;
  private String digitoCodigoBeneficiario;
  private String carteira;
  private String logradouro;
  private String bairro;
  private String cep;
  private String cidade;
  private String uf;
  private String especieDocumento;
}
