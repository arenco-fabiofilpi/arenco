package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.agreements.TituloAReceberDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.utils.FactoryUtils;
import br.com.arenco.arenco_clientes.entities.ReceivableTitleModel;

public record TituloAReceberDtoFactory(ReceivableTitleModel model) {
  public TituloAReceberDto create() {
    return new TituloAReceberDto(
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
        model.getPdfId(),
        model.getPngId());
  }
}
