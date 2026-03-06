package br.com.arenco.arenco_clientes.dtos.agreements;


public record ContratoDto(
    String id,
    String empresa,
    String nomeEmpresa,
    String numeContrato,
    String cliente,
    String nomeCliente,
    String ccusto,
    String nomeCcusto,
    String unidadeDeCusto,
    String valorContrato,
    String dataBase,
    String dtEmissao) {}
