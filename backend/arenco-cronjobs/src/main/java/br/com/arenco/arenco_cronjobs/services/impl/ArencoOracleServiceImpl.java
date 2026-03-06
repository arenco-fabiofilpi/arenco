package br.com.arenco.arenco_cronjobs.services.impl;

import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;
import br.com.arenco.arenco_cronjobs.oracle.entities.ContratoOracle;
import br.com.arenco.arenco_cronjobs.oracle.repositories.ClienteOracleRepository;
import br.com.arenco.arenco_cronjobs.oracle.repositories.ContratoOracleRepository;
import br.com.arenco.arenco_cronjobs.oracle.repositories.TituloAReceberOracleRepository;
import br.com.arenco.arenco_cronjobs.oracle.repositories.TituloRecebidoOracleRepository;
import br.com.arenco.arenco_cronjobs.records.ContratoOracleComTitulosRecord;
import br.com.arenco.arenco_cronjobs.services.ArencoOracleService;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArencoOracleServiceImpl implements ArencoOracleService {
  private final ContratoOracleRepository contratoOracleRepository;
  private final ClienteOracleRepository clienteOracleRepository;
  private final TituloAReceberOracleRepository tituloAReceberOracleRepository;
  private final TituloRecebidoOracleRepository tituloRecebidoOracleRepository;

  @Override
  public Optional<ClienteOracle> pesquisarClienteOracle(final String codigoErp) {
    if (codigoErp == null || codigoErp.isEmpty()) {
      throw new IllegalArgumentException("Código ERP não pode ser nulo ou vazio");
    }
    final Optional<ClienteOracle> clienteOracle =
        clienteOracleRepository.findFirstByCodigo(codigoErp);
    if (clienteOracle.isEmpty()) {
      log.warn("Cliente Oracle com código ERP {} não encontrado", codigoErp);
    }
    return clienteOracle;
  }

  @Override
  public Page<@NonNull ContratoOracleComTitulosRecord> pesquisarContratos(
      final String empresa,
      final String centroDeCusto,
      final Pageable pageable,
      final AtomicInteger contador) {
    log.info(
        "Iniciando pesquisa de contratos. Empresa: {}, Centro de Custo: {}, Página: {}",
        empresa,
        centroDeCusto,
        pageable);

    final var contratosOracle =
        contratoOracleRepository.findContratoOraclesByCcustoAndEmpresa(
            centroDeCusto, empresa, pageable);

    if (contratosOracle.isEmpty()) {
      log.warn(
          "Nenhum contrato encontrado. Empresa: {}, Centro de Custo: {}, Página: {}",
          empresa,
          centroDeCusto,
          pageable);
      return Page.empty(pageable);
    }

    log.debug(
        "Encontrados {} contratos para Empresa: {}, Centro de Custo: {}, Página: {}",
        contratosOracle.getTotalElements(),
        empresa,
        centroDeCusto,
        pageable);

    final long total = contratosOracle.getTotalElements();

    return contratosOracle.map(
        contrato -> {
          int index = contador.incrementAndGet();
          double progresso = (index * 100.0) / total;
          String progressoFormatado = String.format("%.2f%%", progresso);

          log.info(
              "Pesquisando contrato {}/{} ({}) - Número: {}",
              index,
              total,
              progressoFormatado,
              contrato.getNumeContrato());

          return toContratoComTitulosOracleRecord(contrato, empresa);
        });
  }

  private ContratoOracleComTitulosRecord toContratoComTitulosOracleRecord(
      final ContratoOracle contratoOracle, final String empresa) {
    final var codigoContrato = contratoOracle.getNumeContrato();

    final var titulosAReceberOracle =
        tituloAReceberOracleRepository.findAllByNumeDocAndEmpresa(codigoContrato, empresa);
    log.debug(
        "Contrato {} - {} títulos *a receber* encontrados para empresa {}",
        codigoContrato,
        titulosAReceberOracle.size(),
        empresa);

    final var titulosRecebidosOracle =
        tituloRecebidoOracleRepository.findAllByNumeDocAndEmpresa(codigoContrato, empresa);
    log.debug(
        "Contrato {} - {} títulos *recebidos* encontrados para empresa {}",
        codigoContrato,
        titulosRecebidosOracle.size(),
        empresa);

    return new ContratoOracleComTitulosRecord(
        contratoOracle, titulosAReceberOracle, titulosRecebidosOracle);
  }
}
