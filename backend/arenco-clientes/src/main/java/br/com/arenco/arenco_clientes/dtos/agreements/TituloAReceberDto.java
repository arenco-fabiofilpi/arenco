package br.com.arenco.arenco_clientes.dtos.agreements;


public record TituloAReceberDto(
    String id,
    String empresa,
    int cliente,
    String nomeClte,
    String numeDoc,
    String sequencia,
    String lote,
    String dtEmissao,
    String dataBase,
    String ccusto,
    String nomeCcusto,
    String fatura,
    String numeFatura,
    String vencimento,
    String valor,
    String tipoDoc,
    String serie,
    String pdfId,
    String pngId) {}
