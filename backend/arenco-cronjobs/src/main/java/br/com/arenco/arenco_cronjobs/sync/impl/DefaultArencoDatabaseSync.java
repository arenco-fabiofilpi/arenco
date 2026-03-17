package br.com.arenco.arenco_cronjobs.sync.impl;

import br.com.arenco.arenco_cronjobs.exceptions.ClienteSemDocumentoException;
import br.com.arenco.arenco_cronjobs.fetcher.OracleFetcher;
import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;
import br.com.arenco.arenco_cronjobs.records.ContratoOracleComTitulosRecord;
import br.com.arenco.arenco_cronjobs.records.ContratoOracleEClienteModelRecord;
import br.com.arenco.arenco_cronjobs.services.ArencoSincronizacaoClientesService;
import br.com.arenco.arenco_cronjobs.services.ArencoSincronizacaoContratosService;
import br.com.arenco.arenco_cronjobs.sync.ArencoDatabaseSync;
import br.com.arenco.arenco_cronjobs.entities.BoletoAProcessarModel;
import br.com.arenco.arenco_cronjobs.entities.UserModel;
import br.com.arenco.arenco_cronjobs.enums.JobType;
import br.com.arenco.arenco_cronjobs.listeners.ShutdownEventListener;
import br.com.arenco.arenco_cronjobs.services.JobInfoModelService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultArencoDatabaseSync implements ArencoDatabaseSync {

  private final OracleFetcher oracleFetcher;
  private final ShutdownEventListener shutdownListener;
  private final JobInfoModelService jobInfoModelService;
  private final ArencoSincronizacaoClientesService clienteService;
  private final ArencoSincronizacaoContratosService contratoService;

  @Value("${arenco-cronjobs.contratos-replication.centroDeCusto}")
  private String centroDeCustoProperty;

  @Value("${arenco-cronjobs.contratos-replication.empresa}")
  private String empresaProperty;

  @Override
  public void sincronizarBases() {
    final Map<ClienteOracle, List<ContratoOracleComTitulosRecord>> informacoesOracle =
        oracleFetcher.buscarContratosEClientesOracle();
    if (informacoesOracle == null || informacoesOracle.isEmpty()) {
      log.warn("⚠️ Nenhum informação (Clientes e Contratos) foi encontrada no Oracle.");
      return;
    }
    log.info("🔍 Encontrados {} Contratos/Clientes Oracle.", informacoesOracle.size());
    final var clientesModelEContratosOracleRecord = sincronizarClientes(informacoesOracle);
    final List<BoletoAProcessarModel> boletoAProcessarModelList =
        sincronizarContratos(clientesModelEContratosOracleRecord);
    if (boletoAProcessarModelList != null && !boletoAProcessarModelList.isEmpty()) {
      jobInfoModelService.createManualTrigger(
          JobType.SINCRONIZAR_BOLETOS, "Solicitado pela sincronização de bases de dados");
    }
  }

  private List<BoletoAProcessarModel> sincronizarContratos(
      final List<ContratoOracleEClienteModelRecord> clientesModelEContratosOracleRecord) {
    log.info("🚀 Iniciando sincronização de contratos...");
    final long start = System.currentTimeMillis();
    final int total = clientesModelEContratosOracleRecord.size();
    final List<BoletoAProcessarModel> boletoAProcessarModelList = new ArrayList<>();
    int index = 0;

    for (final ContratoOracleEClienteModelRecord record : clientesModelEContratosOracleRecord) {
      index++;
      double porcentagem =
          BigDecimal.valueOf(index * 100.0)
              .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_EVEN)
              .toBigInteger()
              .doubleValue();
      log.info("📊 Progresso: {}/{} ({}%)", index, total, porcentagem);

      for (final var contratoOracleComTitulos : record.contratoOracleComTitulosRecordList()) {
        final var clienteModel = record.userModel();
        final var contratoOracle = contratoOracleComTitulos.contratoOracle();
        final var titulosEmAberto = contratoOracleComTitulos.tituloAReceberOracleList();
        final var titulosFechados = contratoOracleComTitulos.tituloRecebidoOracleList();

        if (shutdownListener.isShuttingDown()) {
          log.error("⛔ Sincronização de contratos interrompida.");
          return boletoAProcessarModelList;
        }

        log.debug(
            "🏢 Sincronizando Contrato: {}. Para o Cliente: {}. Quantidade de Titulos em aberto: {}. Quantidade de titulos pagos: {}",
            contratoOracle.getNumeContrato(),
            clienteModel.getIdErp(),
            titulosEmAberto.size(),
            titulosFechados.size());
        try {
          final List<BoletoAProcessarModel> innerBoletosAProcessarList =
              contratoService.sincronizarContrato(
                  clienteModel, contratoOracle, titulosEmAberto, titulosFechados);
          boletoAProcessarModelList.addAll(innerBoletosAProcessarList);
          log.debug(
              "Adicionado {} Boletos a Processar para o Contrato {}",
              innerBoletosAProcessarList.size(),
              contratoOracle.getNumeContrato());
        } catch (RuntimeException e) {
          log.error(
              "❌ Erro ao sincronizar contrato {}. {}",
              contratoOracle.getNumeContrato(),
              e.getMessage(),
              e);
        }
      }
    }
    log.info("⏱️ Duração da sincronização de contratos: {} ms", System.currentTimeMillis() - start);
    return boletoAProcessarModelList;
  }

  private List<ContratoOracleEClienteModelRecord> sincronizarClientes(
      final Map<ClienteOracle, List<ContratoOracleComTitulosRecord>> informacoesOracle) {
    final var clientesOracle = new ArrayList<>(informacoesOracle.keySet());
    final long start = System.currentTimeMillis();
    log.info("🚀 Iniciando sincronização de clientes...");
    final List<UserModel> userModelList = new ArrayList<>();
    final int total = clientesOracle.size();

    for (int i = 0; i < total; i++) {
      final ClienteOracle clienteOracle = clientesOracle.get(i);

      if (shutdownListener.isShuttingDown()) {
        log.warn("⛔ Sincronização de clientes interrompida.");
        break;
      }

      final int percent = (int) (((i + 1) / (double) total) * 100);
      log.info(
          "📊 Progresso: {}/{} ({}%) - Cliente: {}",
          i + 1, total, percent, clienteOracle.getCodigo());

      try {
        userModelList.add(clienteService.replicateCustomerFromOracleToMongo(clienteOracle));
      } catch (ClienteSemDocumentoException e) {
        log.warn(
            "⚠️ Cliente {} sem documentos válidos: {}", clienteOracle.getCodigo(), e.getMessage());
      } catch (RuntimeException e) {
        log.error(
            "❌ Erro ao sincronizar cliente {}: {}", clienteOracle.getCodigo(), e.getMessage(), e);
      }
    }

    log.info("✅ Clientes sincronizados com sucesso. Total: {}", userModelList.size());
    log.info("⏱️ Duração da sincronização de clientes: {} ms", System.currentTimeMillis() - start);
    return convertCustomerModelList(userModelList, informacoesOracle);
  }

  private List<ContratoOracleEClienteModelRecord> convertCustomerModelList(
      final List<UserModel> customerModelList,
      final Map<ClienteOracle, List<ContratoOracleComTitulosRecord>> informacoesOracle) {
    final var result = new ArrayList<ContratoOracleEClienteModelRecord>();
    for (final var i : informacoesOracle.entrySet()) {
      final var clienteOracle = i.getKey();
      final var customerModelOptional = findCustomerModel(clienteOracle, customerModelList);
      final var listaDeContratosDoCliente = i.getValue();
      customerModelOptional.ifPresent(
          userModel ->
              result.add(
                  new ContratoOracleEClienteModelRecord(userModel, listaDeContratosDoCliente)));
    }
    return result;
  }

  private Optional<UserModel> findCustomerModel(
      final ClienteOracle clienteOracle, final List<UserModel> customerModelList) {
    final var idErp = Integer.parseInt(clienteOracle.getCodigo());
    for (final UserModel customerModel : customerModelList) {
      if (idErp == customerModel.getIdErp()) {
        return Optional.of(customerModel);
      }
    }
    log.error("Cliente com ID ERP {} não foi encontrado", idErp);
    return Optional.empty();
  }
}
