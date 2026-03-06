package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.agreements.ContratoDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.utils.FactoryUtils;
import br.com.arenco.arenco_clientes.entities.AgreementModel;

public record ContratoDtoFactory(AgreementModel model) {
  public ContratoDto create() {
    return new ContratoDto(
        model.getId(),
        model.getEmpresa(),
        model.getNomeEmpresa(),
        model.getNumeContrato(),
        model.getCliente(),
        model.getNomeCliente(),
        model.getCcusto(),
        model.getNomeCcusto(),
        model.getUnidadeDeCusto(),
        FactoryUtils.formatarValorEmReais(model.getValorContrato()),
        ArencoDateUtils.fromLocalDateToString(model.getDataBase()),
        ArencoDateUtils.fromLocalDateToString(model.getDtEmissao()));
  }
}
