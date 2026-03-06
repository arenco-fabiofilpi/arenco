package br.com.arenco.arenco_cronjobs.fetcher.impl;

import br.com.arenco.arenco_cronjobs.fetcher.OracleFetcher;
import br.com.arenco.arenco_cronjobs.oracle.entities.ClienteOracle;
import br.com.arenco.arenco_cronjobs.records.ContratoOracleComTitulosRecord;
import br.com.arenco.arenco_cronjobs.services.ArencoOracleService;
import br.com.arenco.arenco_cronjobs.listeners.ShutdownEventListener;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultOracleFetcher implements OracleFetcher {

  private final ShutdownEventListener shutdownListener;
  private final ArencoOracleService arencoOracleService;

  /** Máximo de páginas a buscar por centro de custo (null = sem limite). */
  @Value("${arenco-cronjobs.sincronizacao.threshold:#{null}}")
  private Integer maxPagesPerCC;

  /** Tamanho padrão de página na busca ao Oracle. */
  @Value("${arenco-cronjobs.oracle.page-size:50}")
  private int defaultPageSize;

  /** Busca contratos no Oracle e retorna um mapa ClienteOracle -> Contratos (com títulos). */
  public Map<ClienteOracle, List<ContratoOracleComTitulosRecord>> buscarContratosEClientesOracle(
      final String empresaProperty, final String centroDeCustoProperty) {

    final long t0 = System.nanoTime();
    log.info(
        "🚀 Iniciando pesquisa no Oracle | empresa='{}' | cc='{}' | pageSize={} | maxPagesPerCC={}",
        empresaProperty,
        centroDeCustoProperty,
        defaultPageSize,
        maxPagesPerCC);

    final List<ContratoOracleComTitulosRecord> contratosOracle =
        findOracleAgreements(empresaProperty, centroDeCustoProperty);

    log.info("🗺️ Montando mapa códigoCliente -> contratos ({} contratos)", contratosOracle.size());
    final Map<String, List<ContratoOracleComTitulosRecord>> porCodigoCliente =
        groupByCodigoCliente(contratosOracle);

    final long t1 = System.nanoTime();
    log.info("👥 Consultando clientes Oracle ({} códigos únicos)…", porCodigoCliente.size());

    final Map<ClienteOracle, List<ContratoOracleComTitulosRecord>> out =
        mapByOracleCustomers(porCodigoCliente);

    final long t2 = System.nanoTime();
    log.info(
        "✅ Concluído. Clientes resolvidos: {} | Contratos: {} | ⏱️ Contratos: {}ms | Clientes: {}ms | Total: {}ms",
        out.size(),
        contratosOracle.size(),
        msBetween(t0, t1),
        msBetween(t1, t2),
        msBetween(t0, t2));

    return out;
  }

  // =========================
  // Etapa 1: Buscar contratos
  // =========================
  private List<ContratoOracleComTitulosRecord> findOracleAgreements(
      final String empresaProperty, final String centroDeCustoProperty) {

    final List<String> centros = extractCentrosDeCusto(centroDeCustoProperty);
    if (centros.isEmpty()) {
      log.warn("⚠️ Nenhum centro de custo informado. Retornando lista vazia.");
      return List.of();
    }

    final List<ContratoOracleComTitulosRecord> acumulado = new ArrayList<>();
    final long tStart = System.nanoTime();

    for (int ccIdx = 0; ccIdx < centros.size(); ccIdx++) {
      final String centro = centros.get(ccIdx);
      if (shutdownListener.isShuttingDown()) {
        log.warn("🛑 Encerrando busca (shutdown solicitado) no início do CC='{}'.", centro);
        break;
      }

      Pageable pageable = PageRequest.of(0, defaultPageSize, Sort.by("numeContrato", "empresa"));
      Page<@NonNull ContratoOracleComTitulosRecord> page;
      final AtomicInteger contador = new AtomicInteger(0); // mantido conforme assinatura do service
      int fetchedPages = 0;
      long ccStart = System.nanoTime();

      log.info("📍 [{} / {}] Iniciando CC='{}'…", ccIdx + 1, centros.size(), centro);

      do {
        if (shutdownListener.isShuttingDown()) {
          log.warn(
              "🛑 Encerrando busca (shutdown solicitado) durante CC='{}', página {}.",
              centro,
              pageable.getPageNumber());
          break;
        }

        final int currentPage = pageable.getPageNumber();
        page = arencoOracleService.pesquisarContratos(empresaProperty, centro, pageable, contador);
        fetchedPages++;

        final int pageSize = page.getNumberOfElements();
        acumulado.addAll(page.getContent());

        final Integer totalPages = page.hasContent() ? page.getTotalPages() : null;
        final long elapsedCC = System.nanoTime() - ccStart;
        final String progressStr = progressPageString(fetchedPages, totalPages);

        log.info(
            "📄 CC='{}' {} | page={} size={} acumulado={} contador={}",
            centro,
            progressStr,
            currentPage,
            pageSize,
            acumulado.size(),
            contador.get());

        pageable = pageable.next();

        // Respeita limite de páginas, se configurado
        if (maxPagesPerCC != null && fetchedPages >= maxPagesPerCC) {
          log.info(
              "⛔ Limite de páginas atingido para CC='{}' ({} páginas em {}).",
              centro,
              fetchedPages,
              human(elapsedCC));
          break;
        }

      } while (page.hasContent());

      log.info(
          "✅ Finalizado CC='{}' | páginasBuscadas={} | acumuladoGlobal={} | ⏱️ {}",
          centro,
          fetchedPages,
          acumulado.size(),
          human(System.nanoTime() - ccStart));
    }

    log.info(
        "📦 Total contratos obtidos: {} | ⏱️ {}",
        acumulado.size(),
        human(System.nanoTime() - tStart));
    return acumulado;
  }

  // ==========================================
  // Etapa 2: Mapear códigoCliente -> contratos
  // ==========================================
  private Map<String, List<ContratoOracleComTitulosRecord>> groupByCodigoCliente(
      final List<ContratoOracleComTitulosRecord> contratos) {

    final Map<String, List<ContratoOracleComTitulosRecord>> map = new LinkedHashMap<>();
    for (ContratoOracleComTitulosRecord c : contratos) {
      final String codigoCliente = c.contratoOracle().getCliente();
      map.computeIfAbsent(codigoCliente, k -> new ArrayList<>()).add(c);
    }
    return map;
  }

  // ===================================
  // Etapa 3: Resolver ClienteOracle(s)
  // ===================================
  private Map<ClienteOracle, List<ContratoOracleComTitulosRecord>> mapByOracleCustomers(
      final Map<String, List<ContratoOracleComTitulosRecord>> porCodigoCliente) {

    final int total = porCodigoCliente.size();
    final long t0 = System.nanoTime();

    // LinkedHashMap para preservar ordem de inserção (opcional)
    final Map<ClienteOracle, List<ContratoOracleComTitulosRecord>> result = new LinkedHashMap<>();

    int idx = 0;
    for (Map.Entry<String, List<ContratoOracleComTitulosRecord>> e : porCodigoCliente.entrySet()) {
      idx++;
      final String codigoCliente = e.getKey();
      final List<ContratoOracleComTitulosRecord> contratosDoCliente = e.getValue();

      if (shutdownListener.isShuttingDown()) {
        log.warn("🛑 Interrompendo resolução de clientes (shutdown) no índice {}/{}.", idx, total);
        break;
      }

      final long stepStart = System.nanoTime();
      final var opt = arencoOracleService.pesquisarClienteOracle(codigoCliente);
      if (opt.isEmpty()) {
        log.warn(
            "⚠️ Cliente '{}' não encontrado. Possui {} contratos. Contratos: {}",
            codigoCliente,
            contratosDoCliente.size(),
            compactContratos(contratosDoCliente));
        continue;
      }

      final ClienteOracle cliente = opt.get();
      if (result.containsKey(cliente)) {
        // Em tese, equals/hashCode no ClienteOracle deve evitar duplicidade; se ocorrer, loga.
        log.error(
            "🔁 Cliente duplicado detectado em result: código='{}'. Ignorando repetição.",
            codigoCliente);
        continue;
      }

      result.put(cliente, contratosDoCliente);

      // Progresso, taxa e ETA
      final long elapsedGlobal = System.nanoTime() - t0;
      final double percent = (idx * 100.0) / total;
      final String rate = ratePerSecond(idx, elapsedGlobal);
      final String eta = etaString(idx, total, elapsedGlobal);

      log.info(
          "👤 [{}/{} | {}] Cliente='{}' resolvido em {} | contratosDoCliente={} | rate={} | ETA~{}",
          idx,
          total,
          String.format("%.1f%%", percent),
          codigoCliente,
          human(System.nanoTime() - stepStart),
          contratosDoCliente.size(),
          rate,
          eta);

      // Log a cada 50 clientes para não poluir
      if (idx % 50 == 0 || idx == total) {
        log.info(
            "📈 Parcial: {}/{} clientes ({}), elapsed={}, ETA~{}",
            idx,
            total,
            String.format("%.1f%%", percent),
            human(elapsedGlobal),
            eta);
      }
    }

    log.info(
        "🏁 Resolução de clientes concluída: {} de {} códigos resolvidos em {}.",
        result.size(),
        total,
        human(System.nanoTime() - t0));
    return result;
  }

  // =========================
  // Helpers
  // =========================
  private List<String> extractCentrosDeCusto(final String property) {
    if (property == null || property.isBlank()) return List.of();
    return Arrays.stream(property.split(","))
        .map(String::trim)
        .filter(s -> !s.isBlank())
        .distinct()
        .collect(Collectors.toList());
  }

  private String progressPageString(int fetched, Integer total) {
    if (total == null || total <= 0) return String.format("page %d/?", fetched);
    double pct = (fetched * 100.0) / total;
    return String.format("page %d/%d (%.1f%%)", fetched, total, pct);
  }

  private static long msBetween(long startNs, long endNs) {
    return Duration.ofNanos(endNs - startNs).toMillis();
  }

  private static String human(long nanos) {
    final Duration d = Duration.ofNanos(nanos);
    long s = d.toSeconds();
    if (s >= 60) {
      long m = s / 60;
      long r = s % 60;
      return String.format("%dm%02ds", m, r);
    }
    long ms = d.toMillisPart();
    return String.format("%ds %dms", s, ms);
  }

  private static String ratePerSecond(int done, long elapsedNanos) {
    if (done <= 0 || elapsedNanos <= 0) return "-/s";
    double perSec = done / (elapsedNanos / 1_000_000_000.0);
    return String.format("%.2f/s", perSec);
  }

  private static String etaString(int done, int total, long elapsedNanos) {
    if (done <= 0 || total <= 0) return "?";
    long remaining = total - done;
    double perSec = done / (elapsedNanos / 1_000_000_000.0);
    if (perSec <= 0.0001) return "?";
    long secs = (long) Math.ceil(remaining / perSec);
    return Duration.ofSeconds(secs).toMinutesPart() > 0
        ? String.format(
            "%dm%02ds",
            Duration.ofSeconds(secs).toMinutes(), Duration.ofSeconds(secs).toSecondsPart())
        : String.format("%ds", secs);
  }

  private static String compactContratos(List<ContratoOracleComTitulosRecord> list) {
    // Evita logar listas enormes; imprime até 5 ids (ou toString do record, caso não haja id)
    int max = Math.min(5, list.size());
    String head = list.stream().limit(max).map(Object::toString).collect(Collectors.joining(", "));
    return list.size() > max ? head + String.format(" …(+%d)", list.size() - max) : head;
  }
}
