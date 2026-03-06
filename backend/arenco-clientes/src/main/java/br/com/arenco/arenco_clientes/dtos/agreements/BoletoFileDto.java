package br.com.arenco.arenco_clientes.dtos.agreements;

import br.com.arenco.arenco_clientes.enums.FileType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoletoFileDto {
  private String id;
  private String dateCreated;
  private String lastUpdated;
  private FileType fileType;
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
}
