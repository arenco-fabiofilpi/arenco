package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.entities.ReceivedTitleModel;
import br.com.arenco.dataseeder.repositories.ReceivedTitleModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedReceivedStrategyImpl extends AbstractDataSeederStrategy {
  private static final String COLLECTION_CODE = "recebidos";

  private final ReceivedTitleModelRepository receivedTitleModelRepository;

  @Override
  public String getCollectionCode() {
    return COLLECTION_CODE;
  }

  void create(final JsonNode doc) {
    final var agreementModel = getAgreementModel(doc);
    final var alreadyExists =
        receivedTitleModelRepository.existsByContratoIdAndSequencia(
            agreementModel.getId(), doc.get("sequencia").asText());
    if (alreadyExists) {
      log.info("Received already exists for agreement with id {}", agreementModel.getId());
      return;
    }
    final var recebido = new ReceivedTitleModel();
    recebido.setContratoId(agreementModel.getId());
    setInfo(recebido, doc);
    receivedTitleModelRepository.save(recebido);
  }

  private void setInfo(final ReceivedTitleModel recebido, final JsonNode doc) {
    recebido.setEmpresa(doc.get("empresa").textValue());
    recebido.setCliente(doc.get("cliente").textValue());
    recebido.setNumeDoc(doc.get("numeDoc").textValue());
    recebido.setSequencia(doc.get("sequencia").textValue());
    recebido.setTipoDoc(doc.get("tipoDoc").textValue());
    recebido.setDescricao(doc.get("descricao").textValue());
    recebido.setSerie(doc.get("serie").textValue());
    recebido.setDataBase(toLocalDate(doc, "dataBase"));
    recebido.setDtEmissao(toLocalDate(doc, "dtEmissao"));
    recebido.setDtDeposito(toLocalDate(doc, "dtDeposito"));
    recebido.setVencimento(toLocalDate(doc, "vencimento"));
    recebido.setMoeda(doc.get("moeda").textValue());
    recebido.setValor(doc.get("valor").doubleValue());
    recebido.setValorJuros(doc.get("valorJuros").doubleValue());
    recebido.setValorDesc(doc.get("valorDesc").doubleValue());
    recebido.setValorPago(doc.get("valorPago").doubleValue());
    recebido.setValorVm(doc.get("valorVm").doubleValue());
    recebido.setValorMulta(doc.get("valorMulta").doubleValue());
    recebido.setObservacao(doc.get("observacao").textValue());
    recebido.setNomeClte(doc.get("nomeClte").textValue());
    recebido.setFatura(doc.get("fatura").textValue());
    recebido.setNumeDeposito(doc.get("numeDeposito").textValue());
    recebido.setDenominacao(doc.get("denominacao").asText());
    recebido.setNomeCcusto(doc.get("nomeCcusto").textValue());
    recebido.setCcusto(doc.get("ccusto").asText());
    recebido.setReceita(doc.get("receita").textValue());
    recebido.setBanco(doc.get("banco").textValue());
    recebido.setValorDeposito(doc.get("valorDeposito").doubleValue());
    recebido.setCodigoBanco(doc.get("codigoBanco").textValue());
    recebido.setAgencia(doc.get("agencia").textValue());
    recebido.setConta(doc.get("conta").textValue());
    recebido.setNomeBanco(doc.get("nomeBanco").textValue());
    recebido.setTipoBaixa(doc.get("tipoBaixa").textValue());
    recebido.setDescricaoTipoBaixa(doc.get("descricaoTipoBaixa").textValue());
    recebido.setDiasAtraso(doc.get("diasAtraso").textValue());
    recebido.setUnidadeDeCusto(doc.get("unidadeDeCusto").textValue());
    recebido.setUnidadeDeCustoOriginal(doc.get("unidadeDeCustoOriginal").textValue());
    recebido.setValorSeguro(doc.get("valorSeguro").doubleValue());
    recebido.setTipoDeposito(doc.get("tipoDeposito").textValue());
    recebido.setDescricaoTipoDeposito(doc.get("descricaoTipoDeposito").textValue());
    recebido.setSequenciaReparcela(doc.get("sequenciaReparcela").textValue());
    recebido.setObservacaoContrato(doc.get("observacaoContrato").textValue());
  }
}
