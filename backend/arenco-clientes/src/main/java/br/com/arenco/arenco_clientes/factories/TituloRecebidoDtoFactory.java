package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.agreements.TituloRecebidoDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.utils.FactoryUtils;
import br.com.arenco.arenco_clientes.entities.ReceivedTitleModel;

public record TituloRecebidoDtoFactory(ReceivedTitleModel model) {
  public TituloRecebidoDto create() {
    return new TituloRecebidoDto(
        model.getId(),
        model.getEmpresa(),
        model.getCliente(),
        model.getNomeClte(),
        model.getNumeDoc(),
        model.getSequencia(),
        model.getLote(),
        ArencoDateUtils.fromLocalDateToString(model.getDtEmissao()),
        ArencoDateUtils.fromLocalDateToString(model.getDataBase()),
        model.getCcusto(),
        model.getNomeCcusto(),
        model.getFatura(),
        model.getNumeFatura(),
        ArencoDateUtils.fromLocalDateToString(model.getVencimento()),
        FactoryUtils.formatarValorEmReais(model.getValor()),
        model.getTipoDoc(),
        model.getSerie(),
        ArencoDateUtils.fromLocalDateToString(model.getDtDeposito()),
        model.getNumeDeposito());
  }
}
