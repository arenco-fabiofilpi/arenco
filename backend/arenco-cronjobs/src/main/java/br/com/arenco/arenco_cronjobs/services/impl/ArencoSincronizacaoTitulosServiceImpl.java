package br.com.arenco.arenco_cronjobs.services.impl;

import br.com.arenco.arenco_cronjobs.mappers.ContratoMapper;
import br.com.arenco.arenco_cronjobs.oracle.entities.TituloAReceberOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.TituloRecebidoOracle;
import br.com.arenco.arenco_cronjobs.services.ArencoSincronizacaoTitulosService;
import br.com.arenco.arenco_cronjobs.entities.AgreementModel;
import br.com.arenco.arenco_cronjobs.entities.BoletoAProcessarModel;
import br.com.arenco.arenco_cronjobs.entities.ReceivableTitleModel;
import br.com.arenco.arenco_cronjobs.entities.ReceivedTitleModel;
import br.com.arenco.arenco_cronjobs.enums.TipoProcessamentoBoleto;
import br.com.arenco.arenco_cronjobs.repositories.ReceivableTitleModelRepository;
import br.com.arenco.arenco_cronjobs.repositories.ReceivedTitleModelRepository;
import br.com.arenco.arenco_cronjobs.services.BoletosService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoSincronizacaoTitulosServiceImpl implements ArencoSincronizacaoTitulosService {
  private final BoletosService boletosService;
  private final ReceivedTitleModelRepository receivedTitleModelRepository;
  private final ReceivableTitleModelRepository receivableTitleModelRepository;

  @Override
  public void sincronizarTitulos(
      final AgreementModel contratoMongo,
      final List<TituloAReceberOracle> titulosAReceber,
      final List<TituloRecebidoOracle> titulosRecebidos) {
    log.debug(
        "Iniciando Sincronizacao de Titulos a Receber. Quantidade: {}. Contrato: {}",
        titulosAReceber.size(),
        contratoMongo.getNumeContrato());

    for (final var tituloAReceber : titulosAReceber) {
      final var tituloAReceberMongo = pegarReceivableTitleModel(contratoMongo, tituloAReceber);
      ContratoMapper.atualizarTituloAReceber(tituloAReceberMongo, tituloAReceber);
      receivableTitleModelRepository.save(tituloAReceberMongo);
      log.debug("Sincronizado titulo a receber sequencia {}", tituloAReceberMongo.getSequencia());
    }

    log.debug(
        "Iniciando Sincronizacao de Titulos Recebidos. Quantidade: {}. Contrato: {}",
        titulosRecebidos.size(),
        contratoMongo.getNumeContrato());

    for (final var tituloRecebido : titulosRecebidos) {
      final var titulosRecebidoMongo = pegarTituloRecebidoMongo(contratoMongo, tituloRecebido);
      ContratoMapper.atualizarTituloRecebido(titulosRecebidoMongo, tituloRecebido);
      receivedTitleModelRepository.save(titulosRecebidoMongo);
      log.debug("Sincronizado titulo recebido sequencia {}", titulosRecebidoMongo.getSequencia());
    }
  }

  @Override
  public void baixarTitulosRecebidos(
      final AgreementModel contratoMongo,
      final List<TituloAReceberOracle> tituloAReceberOracles,
      final List<BoletoAProcessarModel> boletosAProcessar) {
    log.debug(
        "baixarTitulosRecebidos - Iniciando sincronização: remoção de títulos a receber não presentes no Oracle");

    final long quantidadeNoBancoDeDados =
        receivableTitleModelRepository.countAllByContratoId(contratoMongo.getId());
    final int pageSize = 10;
    final int totalPages = (int) Math.ceil((double) quantidadeNoBancoDeDados / pageSize);

    // Pré-processa os títulos do Oracle em um Set para busca rápida
    final Set<String> sequenciasOracle =
        tituloAReceberOracles.stream()
            .map(TituloAReceberOracle::getSequencia)
            .collect(Collectors.toSet());

    for (int page = 0; page < totalPages; page++) {
      final var pageable = PageRequest.of(page, pageSize);
      final var paginaTitulos =
          receivableTitleModelRepository.findByContratoId(contratoMongo.getId(), pageable);

      for (final var titulo : paginaTitulos) {
        if (!sequenciasOracle.contains(titulo.getSequencia())) {
          log.info(
              "baixarTitulo - Removendo titulo recebido sequencia {}, contrato {}",
              titulo.getSequencia(),
              contratoMongo.getNumeContrato());
          final var boletoARemover =
              boletosService.gerarBoletoAProcessar(TipoProcessamentoBoleto.REMOCAO, titulo.getId());
          boletosAProcessar.add(boletoARemover);
          receivableTitleModelRepository.delete(titulo);
        }
      }
    }

    log.debug("baixarTitulosRecebidos - Sincronização de títulos recebidos concluída.");
  }

  private ReceivableTitleModel pegarReceivableTitleModel(
      final AgreementModel contratoMongo, final TituloAReceberOracle tituloAReceberOracle) {
    final var sequencia = tituloAReceberOracle.getSequencia();
    final var tituloAReceberExistenteMongoOpcional =
        receivableTitleModelRepository.findByContratoIdAndSequencia(
            contratoMongo.getId(), sequencia);
    if (tituloAReceberExistenteMongoOpcional.isPresent()) {
      log.debug(
          "Titulo a Receber Sequência {} do Contrato {} já existe",
          sequencia,
          contratoMongo.getNumeContrato());
      return tituloAReceberExistenteMongoOpcional.get();
    }
    final var novoTitulo = new ReceivableTitleModel();
    novoTitulo.setSequencia(sequencia);
    novoTitulo.setContratoId(contratoMongo.getId());
    receivableTitleModelRepository.save(novoTitulo);
    log.info(
        "Novo Titulo a receber sequência {} do Contrato {} armazenado com sucesso",
        sequencia,
        contratoMongo.getNumeContrato());
    return novoTitulo;
  }

  private ReceivedTitleModel pegarTituloRecebidoMongo(
      final AgreementModel contratoMongo, final TituloRecebidoOracle tituloRecebidoOracle) {
    final var sequencia = tituloRecebidoOracle.getSequencia();
    final var tituloRecebidoMongoOptional =
        receivedTitleModelRepository.findByContratoIdAndSequencia(contratoMongo.getId(), sequencia);
    if (tituloRecebidoMongoOptional.isPresent()) {
      log.debug(
          "Titulo Recebido Sequência {} do Contrato {} já existe",
          sequencia,
          contratoMongo.getNumeContrato());
      return tituloRecebidoMongoOptional.get();
    }
    final var novoTitulo = new ReceivedTitleModel();
    novoTitulo.setSequencia(sequencia);
    novoTitulo.setContratoId(contratoMongo.getId());
    receivedTitleModelRepository.save(novoTitulo);
    log.debug(
        "Novo Titulo recebido sequência {} do Contrato {} armazenado com sucesso",
        sequencia,
        contratoMongo.getNumeContrato());
    return novoTitulo;
  }
}
