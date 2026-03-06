package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.agreements.BoletoFileDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.utils.FactoryUtils;
import br.com.arenco.arenco_clientes.entities.BoletoFileModel;
import br.com.arenco.arenco_clientes.entities.ReceivableTitleModel;

public record BoletoFileDtoFactory(
    BoletoFileModel boletoFileModel, ReceivableTitleModel receivableTitleModel) {
  public BoletoFileDto create() {
    final BoletoFileDto dto = new BoletoFileDto();
    dto.setId(boletoFileModel.getId());
    dto.setDateCreated(ArencoDateUtils.fromInstantToString(boletoFileModel.getDateCreated()));
    dto.setLastUpdated(ArencoDateUtils.fromInstantToString(boletoFileModel.getLastUpdated()));
    dto.setFileType(boletoFileModel.getFileType());
    dto.setCliente(receivableTitleModel.getCliente());
    dto.setNomeClte(receivableTitleModel.getNomeClte());
    dto.setNumeDoc(receivableTitleModel.getNumeDoc());
    dto.setSequencia(receivableTitleModel.getSequencia());
    dto.setLote(receivableTitleModel.getLote());
    dto.setDtEmissao(ArencoDateUtils.fromLocalDateToString(receivableTitleModel.getDtEmissao()));
    dto.setDataBase(ArencoDateUtils.fromLocalDateToString(receivableTitleModel.getDataBase()));
    dto.setCcusto(receivableTitleModel.getCcusto());
    dto.setNomeCcusto(receivableTitleModel.getNomeCcusto());
    dto.setFatura(receivableTitleModel.getFatura());
    dto.setNumeFatura(receivableTitleModel.getNumeFatura());
    dto.setVencimento(ArencoDateUtils.fromLocalDateToString(receivableTitleModel.getVencimento()));
    dto.setValor(FactoryUtils.formatarValorEmReais(receivableTitleModel.getValor()));
    dto.setTipoDoc(receivableTitleModel.getTipoDoc());
    dto.setSerie(receivableTitleModel.getSerie());
    dto.setObservacao(receivableTitleModel.getObservacao());
    return dto;
  }
}
