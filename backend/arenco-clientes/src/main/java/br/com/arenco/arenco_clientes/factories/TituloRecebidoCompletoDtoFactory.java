package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.agreements.TituloRecebidoCompletoDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.utils.FactoryUtils;
import br.com.arenco.arenco_clientes.entities.ReceivedTitleModel;

public record TituloRecebidoCompletoDtoFactory(ReceivedTitleModel model) {
  public TituloRecebidoCompletoDto create() {
    final TituloRecebidoCompletoDto dto = new TituloRecebidoCompletoDto();
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
    dto.setUnidadeDeCusto(model.getUnidadeDeCusto());
    dto.setDescricao(model.getDescricao());
    dto.setReceita(model.getReceita());
    dto.setDenominacao(model.getDenominacao());
    dto.setMoeda(model.getMoeda());
    dto.setQuadra(model.getQuadra());
    dto.setSequenciaReparcela(model.getSequenciaReparcela());
    dto.setDataReparcela(model.getDataReparcela());
    dto.setTotalReparcela(FactoryUtils.formatarValorEmReais(model.getTotalReparcela()));
    dto.setDtDeposito(ArencoDateUtils.fromLocalDateToString(model.getDtDeposito()));
    dto.setNumeDeposito(model.getNumeDeposito());
    dto.setValorJuros(FactoryUtils.formatarValorEmReais(model.getValorJuros()));
    dto.setValorDesc(FactoryUtils.formatarValorEmReais(model.getValorDesc()));
    dto.setValorPago(FactoryUtils.formatarValorEmReais(model.getValorPago()));
    dto.setValorVm(FactoryUtils.formatarValorEmReais(model.getValorVm()));
    dto.setValorMulta(FactoryUtils.formatarValorEmReais(model.getValorMulta()));
    dto.setBanco(model.getBanco());
    dto.setValorDeposito(FactoryUtils.formatarValorEmReais(model.getValorDeposito()));
    dto.setCodigoBanco(model.getCodigoBanco());
    dto.setAgencia(model.getAgencia());
    dto.setConta(model.getConta());
    dto.setNomeBanco(model.getNomeBanco());
    dto.setTipoBaixa(model.getTipoBaixa());
    dto.setDescricaoTipoBaixa(model.getDescricaoTipoBaixa());
    dto.setDiasAtraso(model.getDiasAtraso());
    dto.setLoteLoteamento(model.getLoteLoteamento());
    dto.setDataGravacao(model.getDataGravacao());
    dto.setQuadraOriginal(model.getQuadraOriginal());
    dto.setLoteOriginal(model.getLoteOriginal());
    dto.setUnidadeDeCustoOriginal(model.getUnidadeDeCustoOriginal());
    dto.setValorSeguro(FactoryUtils.formatarValorEmReais(model.getValorSeguro()));
    dto.setTipoDeposito(model.getTipoDeposito());
    dto.setDescricaoTipoDeposito(model.getDescricaoTipoDeposito());
    dto.setObservacaoContrato(model.getObservacaoContrato());
    return dto;
  }
}
