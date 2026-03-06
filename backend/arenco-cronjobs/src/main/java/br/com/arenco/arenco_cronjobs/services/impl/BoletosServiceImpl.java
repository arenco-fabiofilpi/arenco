package br.com.arenco.arenco_cronjobs.services.impl;

import br.com.arenco.arenco_cronjobs.entities.*;
import br.com.arenco.arenco_cronjobs.enums.TipoProcessamentoBoleto;
import br.com.arenco.arenco_cronjobs.exceptions.ArencoSincronizacaoBoletosInterrompida;
import br.com.arenco.arenco_cronjobs.listeners.ShutdownEventListener;
import br.com.arenco.arenco_cronjobs.repositories.*;
import br.com.arenco.arenco_cronjobs.services.BoletosService;
import br.com.arenco.arenco_cronjobs.services.S3Service;
import br.com.arenco.arenco_cronjobs.strategies.GerarBoletoStrategy;
import java.time.Clock;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoletosServiceImpl implements BoletosService {
  // ====== Configurações ======
  private static final int BATCH_SIZE = 20;
  private static final int S3_MAX_RETRY = 3;
  private static final long S3_BACKOFF_MS = 400L;

  private final S3Service s3Service;
  private final UserModelRepository userModelRepository;
  private final ShutdownEventListener shutdownEventListener;
  private final AddressModelRepository addressModelRepository;
  private final BoletoFileModelRepository boletoFileModelRepository;
  private final List<GerarBoletoStrategy> estrategiasDeGeracaoDeBoleto;
  private final ReceivableTitleModelRepository receivableTitleModelRepository;
  private final BoletoAProcessarModelRepository boletoAProcessarModelRepository;
  private final PaymentSlipSettingsModelRepository paymentSlipSettingsModelRepository;

  /** Injeta um Clock para facilitar testes; se não houver em seu contexto, pode remover. */
  private final Clock clock = Clock.systemDefaultZone();

  @Override
  public BoletoAProcessarModel gerarBoletoAProcessar(
      final TipoProcessamentoBoleto tipoProcessamentoBoleto, final String receivableTitleId) {
    final var boletoAProcessar = new BoletoAProcessarModel();
    boletoAProcessar.setTipoProcessamentoBoleto(tipoProcessamentoBoleto);
    boletoAProcessar.setReceivableTitleId(receivableTitleId);
    boletoAProcessarModelRepository.save(boletoAProcessar);
    log.info(
        "Salvo Boleto a Processar ID {}, Tipo {}",
        boletoAProcessar.getId(),
        tipoProcessamentoBoleto);
    return boletoAProcessar;
  }

  @Transactional(noRollbackFor = ArencoSincronizacaoBoletosInterrompida.class)
  @Override
  public void processarBoletos() throws ArencoSincronizacaoBoletosInterrompida {
    final long t0 = nowMs();

    final var concluidos = new LongAdder();
    final var falhas = new LongAdder();
    final var inclusoesOk = new LongAdder();
    final var remocoesOk = new LongAdder();
    final var arquivosRemovidos = new LongAdder();
    final var remocoesSemArquivos = new LongAdder();

    final var cfg =
        carregarConfiguracaoAtivaDeBoletos()
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Não existe nenhuma configuração ativa para geração de boletos."));

    final Set<String> processedTitleIds = new HashSet<>();

    long totalSnapshot = 0; // total inicial (para progress)
    int processedSoFar = 0; // itens tratados nesta execução

    final Sort sort = Sort.by(Sort.Direction.ASC, "dateCreated");

    while (true) {
      checkStop();

      // ⚠️ sempre página 0 (deletamos durante o processamento)
      final Page<BoletoAProcessarModel> lote =
          boletoAProcessarModelRepository.findAll(PageRequest.of(0, BATCH_SIZE, sort));

      if (lote.isEmpty()) {
        if (processedSoFar == 0) {
          log.info("[Boletos] Nenhum boleto pendente para processar.");
        }
        break;
      }

      if (totalSnapshot == 0) {
        totalSnapshot = lote.getTotalElements();
        log.info(
            "[Boletos] Iniciando processamento de {} item(ns). Estratégias de geração={}",
            totalSnapshot,
            estrategiasDeGeracaoDeBoleto.size());
      }

      final List<BoletoAProcessarModel> itens = lote.getContent();
      for (int i = 0; i < itens.size(); i++) {
        checkStop();

        final var item = itens.get(i);
        final int idxGlobal = processedSoFar + i + 1;
        logProgresso(t0, idxGlobal - 1, totalSnapshot, item);

        try {
          final var tipo =
              Objects.requireNonNull(
                  item.getTipoProcessamentoBoleto(),
                  "TipoProcessamentoBoleto ausente no item " + safeId(item));

          final String tituloId = safe(item::getReceivableTitleId, "<sem-title>");
          if (!processedTitleIds.add(tituloId)) {
            log.warn(
                "[Boletos] item={} ignorado (título {} já processado neste ciclo).",
                safeId(item),
                tituloId);
            excluirPendente(item.getId());
            concluidos.increment();
            continue;
          }

          switch (tipo) {
            case INCLUSAO ->
                incluirBoleto(item, cfg, concluidos, inclusoesOk, t0, idxGlobal, totalSnapshot);
            case REMOCAO ->
                removerBoleto(
                    item,
                    remocoesSemArquivos,
                    arquivosRemovidos,
                    concluidos,
                    remocoesOk,
                    t0,
                    idxGlobal,
                    totalSnapshot);
            default ->
                throw new IllegalArgumentException("TipoProcessamentoBoleto desconhecido: " + tipo);
          }

        } catch (final ArencoSincronizacaoBoletosInterrompida e) {
          // propaga imediatamente (não queremos continuar)
          throw e;
        } catch (final RuntimeException e) {
          falhas.increment();
          log.error(
              "[Boletos] ❌ item={} falhou {}/{} ({}). Motivo: {}",
              safeId(item),
              idxGlobal,
              totalSnapshot,
              pctClamped(idxGlobal, (int) totalSnapshot),
              e.getMessage(),
              e);
        }
      }

      processedSoFar += itens.size();
      // próxima iteração volta a pegar a página 0
    }

    final long totalMs = nowMs() - t0;
    log.info(
        "[Boletos] FIM — processados: {}, falhas: {}, total: {}, tempo total: {} | inclusões OK: {} | remoções OK: {} | arquivos removidos: {} | remoções sem arquivos: {}",
        concluidos.sum(),
        falhas.sum(),
        processedSoFar,
        fmtDuration(totalMs),
        inclusoesOk.sum(),
        remocoesOk.sum(),
        arquivosRemovidos.sum(),
        remocoesSemArquivos.sum());
  }

  private static String safeId(final Object obj) {
    try {
      final var m = obj.getClass().getMethod("getId");
      final Object id = m.invoke(obj);
      return id == null ? "<sem-id>" : String.valueOf(id);
    } catch (Exception ignore) {
      return "<sem-id>";
    }
  }

  private static <T> T safe(final Supplier<T> s, final T fallback) {
    try {
      return s.get();
    } catch (Exception e) {
      return fallback;
    }
  }

  private static String pct(final int parte, final int total) {
    if (total <= 0) return "0%";
    final double p = (parte * 100.0) / total;
    return String.format(Locale.US, "%.1f%%", p);
  }

  private static double percent(final int parte, final int total) {
    if (total <= 0) return 0d;
    return Math.max(0d, Math.min(1d, (parte * 1.0) / total));
  }

  private static String fmtDuration(long ms) {
    if (ms < 0) ms = 0;
    final long s = TimeUnit.MILLISECONDS.toSeconds(ms);
    final long h = s / 3600;
    final long m = (s % 3600) / 60;
    final long ss = s % 60;
    if (h > 0) return String.format("%dh %02dm %02ds", h, m, ss);
    if (m > 0) return String.format("%dm %02ds", m, ss);
    return String.format("%ds", ss);
  }

  private static long estimateEta(final long elapsedMs, final int done, final int total) {
    if (done <= 0 || total <= 0) return 0L;
    final long avgPerItem = elapsedMs / done;
    final int remaining = Math.max(0, total - done);
    return avgPerItem * remaining;
  }

  private static String progressBar(double progress01) {
    final int width = 20;
    progress01 = Math.max(0, Math.min(1, progress01));
    final int filled = (int) Math.round(progress01 * width);
    final StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < width; i++) {
      sb.append(i < filled ? '#' : ' ');
    }
    sb.append("]");
    final int pct = (int) Math.round(progress01 * 100);
    sb.append(' ').append(String.format("%3d%%", pct));
    return sb.toString();
  }

  private static String pctClamped(final int parte, final int total) {
    if (total <= 0) return "0%";
    final double p = Math.min(100.0, (parte * 100.0) / total);
    return String.format(Locale.US, "%.1f%%", p);
  }

  private void logProgresso(final long t0, final int feitos, final long total, final Object item) {
    final long elapsedAntes = nowMs() - t0;
    final long etaAntes = estimateEta(elapsedAntes, feitos, (int) total);
    log.info(
        "[Boletos] {} {}/{} ({}) | decorrido={} | ETA≈{} | iniciando item={}",
        progressBar(percent(feitos, (int) total)),
        feitos,
        total,
        pctClamped(feitos, (int) total),
        fmtDuration(elapsedAntes),
        fmtDuration(etaAntes),
        safeId(item));
  }

  @Transactional(noRollbackFor = ArencoSincronizacaoBoletosInterrompida.class)
  protected void removerBoleto(
      final BoletoAProcessarModel item,
      final LongAdder remocoesSemArquivos,
      final LongAdder arquivosRemovidos,
      final LongAdder concluidos,
      final LongAdder remocoesOk,
      final long t0,
      final int idxGlobal,
      final long totalElements)
      throws ArencoSincronizacaoBoletosInterrompida {

    checkStop();

    final var receivableTitle =
        receivableTitleModelRepository
            .findById(item.getReceivableTitleId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "ReceivableTitle não encontrado: " + item.getReceivableTitleId()));

    final var arquivos =
        Optional.ofNullable(
                boletoFileModelRepository.findAllByReceivableTitleId(receivableTitle.getId()))
            .orElseGet(List::of);

    if (arquivos.isEmpty()) {
      remocoesSemArquivos.increment();
      log.warn(
          "[Boletos] REMOCAO item={} | Nenhum arquivo de boleto para o título {}",
          safeId(item),
          receivableTitle.getId());
    } else {
      final int removidos = removerArquivos(arquivos);
      arquivosRemovidos.add(removidos);
      log.info(
          "[Boletos] REMOCAO item={} | {} arquivo(s) removido(s) para o título {}",
          safeId(item),
          removidos,
          receivableTitle.getId());
    }

    excluirPendente(item.getId());
    concluidos.increment();
    remocoesOk.increment();

    final long elapsedDepois = nowMs() - t0;
    final long etaDepois = estimateEta(elapsedDepois, idxGlobal, (int) totalElements);
    log.info(
        "[Boletos] ✅ REMOCAO item={} concluído ({}/{}, {}) | decorrido={} | ETA≈{}",
        safeId(item),
        idxGlobal,
        totalElements,
        pct(idxGlobal, (int) totalElements),
        fmtDuration(elapsedDepois),
        fmtDuration(etaDepois));
  }

  @Transactional(noRollbackFor = ArencoSincronizacaoBoletosInterrompida.class)
  protected void incluirBoleto(
      final BoletoAProcessarModel item,
      final PaymentSlipSettingsModel cfg,
      final LongAdder concluidos,
      final LongAdder inclusoesOk,
      final long t0,
      final int idxGlobal,
      final long totalElements)
      throws ArencoSincronizacaoBoletosInterrompida {

    checkStop();

    final var info = getNeededInfoToGeneratePaymentSlip(item.getReceivableTitleId(), cfg);

    for (final var estrategia : estrategiasDeGeracaoDeBoleto) {
      checkStop(); // interrupção entre estratégias
      estrategia.gerar(
          info.userModel,
          info.addressModel,
          info.receivableTitleModel,
          info.paymentSlipSettingsModel);
    }

    excluirPendente(item.getId());
    concluidos.increment();
    inclusoesOk.increment();

    final long elapsedDepois = nowMs() - t0;
    final long etaDepois = estimateEta(elapsedDepois, idxGlobal, (int) totalElements);
    log.info(
        "[Boletos] ✅ INCLUSAO item={} concluído ({}/{}, {}) | decorrido={} | ETA≈{}",
        safeId(item),
        idxGlobal,
        totalElements,
        pct(idxGlobal, (int) totalElements),
        fmtDuration(elapsedDepois),
        fmtDuration(etaDepois));
  }

  @Transactional(noRollbackFor = ArencoSincronizacaoBoletosInterrompida.class)
  protected void excluirPendente(final String id) {
    try {
      boletoAProcessarModelRepository.deleteById(id);
    } catch (DataAccessException e) {
      log.warn(
          "[Boletos] Falha ao remover item pendente id={} (seguindo). Motivo={}",
          id,
          e.getMessage());
    }
  }

  @Transactional(readOnly = true)
  protected NeededInfoToGeneratePaymentSlip getNeededInfoToGeneratePaymentSlip(
      final String receivableTitleModelId, final PaymentSlipSettingsModel cfgAtiva) {

    final var receivableTitle =
        receivableTitleModelRepository
            .findById(receivableTitleModelId)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "ReceivableTitle não encontrado: " + receivableTitleModelId));

    final var userModelIdErp = receivableTitle.getCliente();
    final var userModel =
        userModelRepository
            .findFirstByIdErp(userModelIdErp)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "ReceivableTitle "
                            + receivableTitle.getId()
                            + " possui Cliente inválido (idErp="
                            + userModelIdErp
                            + ")"));

    final var addressModel =
        addressModelRepository
            .findFirstByUserId(userModel.getId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "UserModel " + userModel.getId() + " não possui endereço"));

    return new NeededInfoToGeneratePaymentSlip(userModel, addressModel, receivableTitle, cfgAtiva);
  }

  @Transactional(readOnly = true)
  protected Optional<PaymentSlipSettingsModel> carregarConfiguracaoAtivaDeBoletos() {
    try {
      // ideal: repository com metodo explícito por "active"
      final var todos =
          paymentSlipSettingsModelRepository.findAll(
              Sort.by(Sort.Direction.DESC, "lastUpdated", "dateCreated"));
      return todos.isEmpty() ? Optional.empty() : Optional.of(todos.getFirst());
    } catch (final DataAccessException t) {
      final var all = paymentSlipSettingsModelRepository.findAll();
      return all.isEmpty() ? Optional.empty() : Optional.of(all.getFirst());
    }
  }

  private int removerArquivos(final List<BoletoFileModel> arquivos)
      throws ArencoSincronizacaoBoletosInterrompida {
    int removidos = 0;
    for (final var arquivo : arquivos) {
      try {
        remover(arquivo);
        removidos++;
      } catch (final RuntimeException ex) {
        log.error(
            "[Boletos] Falha ao remover arquivo={} | Motivo={}",
            safeId(arquivo),
            ex.getMessage(),
            ex);
      }
    }
    return removidos;
  }

  private void remover(final BoletoFileModel boletoFileModel)
      throws ArencoSincronizacaoBoletosInterrompida {

    checkStop();

    // Retry resiliente no delete S3
    s3WithRetryVoid(() -> s3Service.delete(boletoFileModel.getId()));

    // Mesmo que o S3 falhe, seguimos com a remoção do registro (idempotência)
    boletoFileModelRepository.delete(boletoFileModel);
  }

  // ================== Auxiliares ==================

  private long nowMs() {
    return clock.millis();
  }

  private void checkStop() throws ArencoSincronizacaoBoletosInterrompida {
    if (shutdownEventListener.isShuttingDown()) {
      throw new ArencoSincronizacaoBoletosInterrompida("Solicitada interrupção da sincronização.");
    }
  }

  // ====== Utilitários de retry S3 (simples) ======

  private void s3WithRetryVoid(final Runnable action)
      throws ArencoSincronizacaoBoletosInterrompida {
    RuntimeException last = null;
    for (int i = 1; i <= S3_MAX_RETRY; i++) {
      try {
        action.run();
        return;
      } catch (NoSuchBucketException e) {
        // erro crítico e determinístico: não adianta retry
        throw new ArencoSincronizacaoBoletosInterrompida("O Bucket não existe");
      } catch (RuntimeException e) {
        last = e;
        if (i < S3_MAX_RETRY) {
          sleep(S3_BACKOFF_MS * i);
        }
      }
    }
    // nesse caso, apenas loga e segue; a remoção do registro é independente do S3
    log.warn(
        "[Boletos] delete no S3 falhou definitivamente (seguindo com remoção no DB). Motivo={}",
        last.getMessage());
  }

  private static void sleep(final long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
    }
  }

  // ====== DTO ======
  protected record NeededInfoToGeneratePaymentSlip(
      UserModel userModel,
      AddressModel addressModel,
      ReceivableTitleModel receivableTitleModel,
      PaymentSlipSettingsModel paymentSlipSettingsModel) {}
}
