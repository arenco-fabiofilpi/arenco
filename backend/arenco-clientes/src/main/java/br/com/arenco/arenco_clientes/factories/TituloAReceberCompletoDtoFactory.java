package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.agreements.TituloAReceberCompletoDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.utils.FactoryUtils;
import br.com.arenco.arenco_clientes.entities.ReceivableTitleModel;

public record TituloAReceberCompletoDtoFactory(ReceivableTitleModel model) {
  public TituloAReceberCompletoDto create() {
    final var dto = new TituloAReceberCompletoDto();
    dto.setId(model.getId());
    dto.setEmpresa(model.getEmpresa());
    dto.setCliente(model.getCliente());
    dto.setNomeClte(model.getNomeClte());
    dto.setNumeDoc(model.getNumeDoc());
    dto.setSequencia(model.getSequencia());
    dto.setLote(model.getLote());
    dto.setDtEmissao(ArencoDateUtils.fromLocalDateToString(model.getDtEmissao()));
    dto.setDataBase(ArencoDateUtils.fromLocalDateToString(model.getDataBase()));
    dto.setCcusto(model.getCcusto());
    dto.setNomeCcusto(model.getNomeCcusto());
    dto.setFatura(model.getFatura());
    dto.setNumeFatura(model.getNumeFatura());
    dto.setVencimento(ArencoDateUtils.fromLocalDateToString(model.getVencimento()));
    dto.setValor(FactoryUtils.formatarValorEmReais(model.getValor()));
    dto.setTipoDoc(model.getTipoDoc());
    dto.setSerie(model.getSerie());
    dto.setObservacao(model.getObservacao());
    dto.setSaldo(FactoryUtils.formatarValorEmReais(model.getSaldo()));
    dto.setUnidadeDeCusto(model.getUnidadeDeCusto());
    dto.setDescricao(model.getDescricao());
    dto.setReceita(model.getReceita());
    dto.setDenominacao(model.getDenominacao());
    dto.setDataDeHoje(ArencoDateUtils.fromLocalDateToString(model.getDataDeHoje()));
    dto.setMoeda(model.getMoeda());
    dto.setDescricaoMoeda(model.getDescricaoMoeda());
    dto.setQtdeMoeda(model.getQtdeMoeda());
    dto.setSaldoQtdeMoeda(model.getSaldoQtdeMoeda());
    dto.setValorCorrigidoVencimento(
        FactoryUtils.formatarValorEmReais(model.getValorCorrigidoVencimento()));
    dto.setValorCorrigidoHoje(FactoryUtils.formatarValorEmReais(model.getValorCorrigidoHoje()));
    dto.setValorCorrigidoDataRef(
        FactoryUtils.formatarValorEmReais(model.getValorCorrigidoDataRef()));
    dto.setCodigoPortador(model.getCodigoPortador());
    dto.setTemCobrancaBoleto(model.getTemCobrancaBoleto());
    dto.setQuadra(model.getQuadra());
    dto.setLote(model.getLote());
    dto.setIndicacaoSituacaoJuridica(model.getIndicacaoSituacaoJuridica());
    dto.setParcelasLiberadaApos(model.getParcelasLiberadaApos());
    dto.setTipoCorrecaoContrato(model.getTipoCorrecaoContrato());
    dto.setTaxaJurosValorPresente(model.getTaxaJurosValorPresente());
    dto.setSaldoValorPresente(FactoryUtils.formatarValorEmReais(model.getSaldoValorPresente()));
    dto.setSeguroEmbutidoParcela(model.getSeguroEmbutidoParcela());
    dto.setPercentualSeguro(model.getPercentualSeguro());
    dto.setCodigoCobranca(model.getCodigoCobranca());
    dto.setCnpjCpfDoCliente(model.getCnpjCpfDoCliente());
    dto.setSequenciaReparcela(model.getSequenciaReparcela());
    dto.setDataReparcela(model.getDataReparcela());
    dto.setTotalReparcela(model.getTotalReparcela());
    dto.setPdfId(model.getPdfId());
    dto.setPngId(model.getPngId());
    return dto;
  }
}
