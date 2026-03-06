package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.repositories.BoletoFileModelRepository;
import br.com.arenco.arenco_clientes.services.BoletosRemocaoService;
import br.com.arenco.arenco_clientes.services.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoletosRemocaoServiceImpl implements BoletosRemocaoService {
  private final S3Service s3Service;
  private final BoletoFileModelRepository boletoFileModelRepository;

  @Override
  public void removerBoletos() {
    final long t0 = System.currentTimeMillis();

    final long totalInicial = boletoFileModelRepository.count();
    if (totalInicial == 0) {
      log.info("[Boletos:Remocao] Nenhum boleto para remover.");
      return;
    }

    final int pageSize = 50; // ajuste conforme IO/memória
    long processados = 0;
    long s3Ok = 0;
    long s3Falha = 0;
    long dbOk = 0;

    int rodadasSemProgresso = 0;
    final int maxSemProgresso = 3; // evita loop se nada for removido

    log.info(
        "[Boletos:Remocao] Iniciando remoção em massa de {} arquivo(s). pageSize={}",
        totalInicial,
        pageSize);

    while (processados < totalInicial) {
      processados++;
      final var page =
          boletoFileModelRepository.findAll(
              PageRequest.of(0, pageSize, Sort.by("dateCreated").ascending()));

      if (page.isEmpty()) break;

      long removidosNestaRodada = 0;

      for (final var boleto : page.getContent()) {
        final String barraAntes = progressBar(processados / (double) totalInicial);
        final long elapsedAntes = System.currentTimeMillis() - t0;
        final long etaAntes = estimateEta(elapsedAntes, processados, totalInicial);

        log.info(
            "[Boletos:Remocao] {} {}/{} ({}) | decorrido={} | ETA≈{} | removendo id={}",
            barraAntes,
            processados,
            totalInicial,
            pct(processados, totalInicial),
            fmtDuration(elapsedAntes),
            fmtDuration(etaAntes),
            safeId(boleto));

        try {
          try {
            s3Service.delete(boleto.getId());
            s3Ok++;
          } catch (final RuntimeException ex) {
            s3Falha++;
            log.warn(
                "[Boletos:Remocao] id={} | Falha ao deletar no S3: {} (seguindo com remoção no Mongo)",
                safeId(boleto),
                ex.getMessage(),
                ex);
          }

          boletoFileModelRepository.delete(boleto);
          dbOk++;
          removidosNestaRodada++;

          final String barraDepois = progressBar(processados / (double) totalInicial);
          final long elapsedDepois = System.currentTimeMillis() - t0;
          final long etaDepois = estimateEta(elapsedDepois, processados, totalInicial);

          log.info(
              "[Boletos:Remocao] ✅ id={} removido ({}/{}, {}) | decorrido={} | ETA≈{}",
              safeId(boleto),
              processados,
              totalInicial,
              pct(processados, totalInicial),
              fmtDuration(elapsedDepois),
              fmtDuration(etaDepois));

        } catch (final Exception e) {
          log.error(
              "[Boletos:Remocao] ❌ id={} | Erro na remoção: {}", safeId(boleto), e.getMessage(), e);
        }
      }

      if (removidosNestaRodada == 0) {
        rodadasSemProgresso++;
        if (rodadasSemProgresso >= maxSemProgresso) {
          log.error(
              "[Boletos:Remocao] Abortando: nenhuma remoção em {} rodada(s). Itens problemáticos podem permanecer.",
              maxSemProgresso);
          break;
        } else {
          log.warn(
              "[Boletos:Remocao] Nenhuma remoção nesta rodada; tentando novamente ({}/{})",
              rodadasSemProgresso,
              maxSemProgresso);
        }
      } else {
        rodadasSemProgresso = 0;
      }
    }

    final long totalMs = System.currentTimeMillis() - t0;
    log.info(
        "[Boletos:Remocao] FIM — processados={}, s3Ok={}, s3Falha={}, dbOk={}, tempo total={}",
        processados,
        s3Ok,
        s3Falha,
        dbOk,
        fmtDuration(totalMs));
  }

  /* ===== Helpers (copie se não tiver na classe) ===== */

  private static String safeId(Object obj) {
    try {
      var m = obj.getClass().getMethod("getId");
      Object id = m.invoke(obj);
      return id == null ? "<sem-id>" : String.valueOf(id);
    } catch (Exception ignore) {
      return "<sem-id>";
    }
  }

  private static String pct(long parte, long total) {
    if (total <= 0L) return "0%";
    double p = (parte * 100.0) / total;
    return String.format(java.util.Locale.US, "%.1f%%", p);
  }

  private static String fmtDuration(long ms) {
    if (ms < 0) ms = 0;
    long s = ms / 1000;
    long h = s / 3600;
    long m = (s % 3600) / 60;
    long ss = s % 60;
    if (h > 0) return String.format("%dh %02dm %02ds", h, m, ss);
    if (m > 0) return String.format("%dm %02ds", m, ss);
    return String.format("%ds", ss);
  }

  private static long estimateEta(long elapsedMs, long done, long total) {
    if (done <= 0 || total <= 0) return 0L;
    long avgPerItem = elapsedMs / done;
    long remaining = Math.max(0L, total - done);
    return avgPerItem * remaining;
  }

  private static String progressBar(double progress01) {
    final int width = 24;
    if (progress01 < 0) progress01 = 0;
    if (progress01 > 1) progress01 = 1;
    int filled = (int) Math.round(progress01 * width);
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < width; i++) sb.append(i < filled ? '#' : ' ');
    sb.append("] ").append(String.format("%3d%%", (int) Math.round(progress01 * 100)));
    return sb.toString();
  }
}
