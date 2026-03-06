package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.entities.ReceivableTitleModel;
import br.com.arenco.dataseeder.repositories.ReceivableTitleModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedReceivablesStrategyImpl extends AbstractDataSeederStrategy {
  private static final String COLLECTION_CODE = "aReceber";

  private final ReceivableTitleModelRepository receivableTitleModelRepository;

  @Override
  public String getCollectionCode() {
    return COLLECTION_CODE;
  }

  void create(final JsonNode doc) {
    final var agreementModel = getAgreementModel(doc);
    final var alreadyExists =
        receivableTitleModelRepository.existsByContratoIdAndSequencia(
            agreementModel.getId(), doc.get("sequencia").asText());
    if (alreadyExists) {
      log.info("Receivables already exists for agreement with id {}", agreementModel.getId());
      return;
    }
    final var aReceber = new ReceivableTitleModel();
    aReceber.setContratoId(agreementModel.getId());
    setInfo(aReceber, doc);
    receivableTitleModelRepository.save(aReceber);
  }

  private void setInfo(final ReceivableTitleModel aReceber, final JsonNode doc) {
    aReceber.setCcusto(doc.get("ccusto").asText());
    aReceber.setCliente(doc.get("cliente").intValue());
    aReceber.setCnpjCpfDoCliente(doc.get("cnpjCpfDoCliente").asText());
    aReceber.setDataBase(toLocalDate(doc, "dataBase"));
    aReceber.setDenominacao(doc.get("denominacao").asText());
    aReceber.setDescricao(doc.get("descricao").textValue());
    aReceber.setDescricaoMoeda(doc.get("descricaoMoeda").textValue());
    aReceber.setDtEmissao(toLocalDate(doc, "dtEmissao"));
    aReceber.setEmpresa(doc.get("empresa").textValue());
    aReceber.setFatura(doc.get("fatura").textValue());
    aReceber.setIndicacaoSituacaoJuridica(doc.get("indicacaoSituacaoJuridica").textValue());
    aReceber.setMoeda(doc.get("moeda").textValue());
    aReceber.setNomeCcusto(doc.get("nomeCcusto").textValue());
    aReceber.setNomeClte(doc.get("nomeClte").textValue());
    aReceber.setNumeDoc(doc.get("numeDoc").textValue());
    aReceber.setQtdeMoeda(doc.get("qtdeMoeda").doubleValue());
    aReceber.setReceita(doc.get("receita").textValue());
    aReceber.setSaldo(doc.get("saldo").doubleValue());
    aReceber.setSaldoQtdeMoeda(doc.get("saldoQtdeMoeda").doubleValue());
    aReceber.setSaldoValorPresente(doc.get("saldoValorPresente").doubleValue());
    aReceber.setSeguroEmbutidoParcela(doc.get("seguroEmbutidoParcela").textValue());
    aReceber.setSequencia(doc.get("sequencia").textValue());
    aReceber.setSequenciaReparcela(doc.get("sequenciaReparcela").textValue());
    aReceber.setSerie(doc.get("serie").textValue());
    aReceber.setTaxaJurosValorPresente(doc.get("taxaJurosValorPresente").textValue());
    aReceber.setTemCobrancaBoleto(doc.get("temCobrancaBoleto").textValue());
    aReceber.setTipoCorrecaoContrato(doc.get("tipoCorrecaoContrato").textValue());
    aReceber.setTipoDoc(doc.get("tipoDoc").textValue());
    aReceber.setUnidadeDeCusto(doc.get("unidadeDeCusto").textValue());
    aReceber.setValor(doc.get("valor").doubleValue());
    aReceber.setValorCorrigidoDataRef(doc.get("valorCorrigidoDataRef").doubleValue());
    aReceber.setValorCorrigidoHoje(doc.get("valorCorrigidoHoje").doubleValue());
    aReceber.setValorCorrigidoVencimento(doc.get("valorCorrigidoVencimento").doubleValue());
    aReceber.setVencimento(toLocalDate(doc, "vencimento"));
  }
}
