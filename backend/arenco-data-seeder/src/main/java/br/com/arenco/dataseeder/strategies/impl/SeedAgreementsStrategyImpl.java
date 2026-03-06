package br.com.arenco.dataseeder.strategies.impl;

import br.com.arenco.dataseeder.entities.AgreementModel;
import br.com.arenco.dataseeder.repositories.AgreementModelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeedAgreementsStrategyImpl extends AbstractDataSeederStrategy {
  private static final String COLLECTION_CODE = "contratos";

  private final AgreementModelRepository agreementModelRepository;

  @Override
  public String getCollectionCode() {
    return COLLECTION_CODE;
  }

  void create(final JsonNode doc) {
    final var userModel = getUserModel(doc);
    final var numeContrato = doc.get("numeContrato").asText();
    final var empresa = doc.get("empresa").asText();
    final var alreadyExists =
        agreementModelRepository.existsByNumeContratoAndEmpresaAndUserId(
            numeContrato, empresa, userModel.getId());
    if (alreadyExists) {
      log.info("Agreement already exists");
      return;
    }
    final var agreementModel = new AgreementModel();
    agreementModel.setUserId(userModel.getId());
    agreementModel.setEmpresa(empresa);
    agreementModel.setNomeEmpresa(doc.get("nomeEmpresa").asText());
    agreementModel.setNumeContrato(numeContrato);
    agreementModel.setCliente(doc.get("cliente").asText());
    agreementModel.setNomeCliente(doc.get("nomeCliente").asText());
    agreementModel.setCcusto(doc.get("ccusto").asText());
    agreementModel.setNomeCcusto(doc.get("nomeCcusto").asText());
    agreementModel.setEnderecoCcusto(doc.get("enderecoCcusto").asText());
    agreementModel.setUnidadeDeCusto(doc.get("unidadeDeCusto").asText());
    agreementModel.setReceita(doc.get("receita").asText());
    agreementModel.setDenominacao(doc.get("denominacao").asText());
    agreementModel.setSequenciaInicial(doc.get("sequenciaInicial").asText());
    agreementModel.setValorContrato(doc.get("valorContrato").asDouble());
    agreementModel.setValorContratoExtenso(doc.get("valorContratoExtenso").asText());
    agreementModel.setTipoDoc(doc.get("tipoDoc").asText());
    agreementModel.setDescricao(doc.get("descricao").asText());
    agreementModel.setObservacaoContrato(doc.get("observacaoContrato").asText());
    agreementModel.setCic(doc.get("cic").asText());
    agreementModel.setEnderecoCompleto(doc.get("enderecoCompleto").asText());
    agreementModel.setCidade(doc.get("cidade").asText());
    agreementModel.setEstado(doc.get("estado").asText());
    agreementModel.setCepCompleto(doc.get("cepCompleto").asText());
    agreementModel.setTipoCcusto(doc.get("tipoCcusto").asText());
    agreementModel.setIndicacaoSituacaoJuridica(doc.get("indicacaoSituacaoJuridica").asText());
    agreementModel.setCidadeEmpresa(doc.get("cidadeEmpresa").asText());
    agreementModel.setCnpjEmpresa(doc.get("cnpjEmpresa").asText());
    agreementModel.setEnderecoCompletoEmpresa(doc.get("enderecoCompletoEmpresa").asText());
    agreementModel.setBairro(doc.get("bairro").asText());
    agreementModel.setTipoCorrecaoContrato(doc.get("tipoCorrecaoContrato").asText());
    agreementModel.setCpfConjugue(doc.get("cpfConjugue").asText());
    agreementModel.setCodigoNomeCliente(doc.get("codigoNomeCliente").asText());
    agreementModel.setEnderecoCompletoCliente(doc.get("enderecoCompletoCliente").asText());
    agreementModel.setTelefones(doc.get("telefones").asText());
    agreementModel.setUnidadeQuadraLote(doc.get("unidadeQuadraLote").asText());
    agreementModel.setTipoUnidade(doc.get("tipoUnidade").asText());
    agreementModel.setUsuarioInclusao(doc.get("usuarioInclusao").asText());
    agreementModel.setSeguroEmbutidoParcela(doc.get("seguroEmbutidoParcela").asText());
    agreementModel.setTaxaJurosValorPresente(doc.get("taxaJurosValorPresente").asText());
    agreementModel.setNumeContratoOriginal(doc.get("numeContratoOriginal").asText());
    agreementModel.setUnidadeDeCustoOriginal(doc.get("unidadeDeCustoOriginal").asText());
    agreementModel.setTotalSaldoEmAberto(doc.get("totalSaldoEmAberto").asText());
    agreementModel.setSemCarenciaArredJurosMulta(doc.get("semCarenciaArredJurosMulta").asText());
    agreementModelRepository.save(agreementModel);
  }
}
