package br.com.arenco.arenco_clientes.entities;

import br.com.arenco.arenco_clientes.enums.Bank;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document
public class PaymentSlipSettingsModel {
  @Id private String id;
  @CreatedDate private Instant dateCreated;
  @LastModifiedDate private Instant lastUpdated;
  @Version
  private Long version;

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
