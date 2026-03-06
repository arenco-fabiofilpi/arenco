package br.com.arenco.arenco_cronjobs.orchestrator.impl;

import br.com.arenco.arenco_cronjobs.entities.JobInfoModel;
import br.com.arenco.arenco_cronjobs.enums.JobType;
import br.com.arenco.arenco_cronjobs.enums.SyncStatus;
import br.com.arenco.arenco_cronjobs.orchestrator.SyncOrchestrator;
import br.com.arenco.arenco_cronjobs.repositories.JobInfoModelRepository;
import br.com.arenco.arenco_cronjobs.services.BoletosService;
import br.com.arenco.arenco_cronjobs.sync.ArencoDatabaseSync;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncOrchestratorImpl implements SyncOrchestrator {

  private final ArencoDatabaseSync arencoDatabaseSync;
  private final BoletosService boletosService;
  private final JobInfoModelRepository jobRepo;
  private final MongoTemplate mongoTemplate;

  // Concorrência por TIPO: evita 2 execuções simultâneas do MESMO tipo
  private final Map<JobType, AtomicBoolean> runningByType = new ConcurrentHashMap<>();

  private AtomicBoolean runningFlag(final JobType type) {
    return runningByType.computeIfAbsent(type, t -> new AtomicBoolean(false));
  }

  /**
   * Dispara um job: se já houver job do mesmo tipo rodando, enfileira (QUEUED); senão marca RUNNING
   * e dispara em background. Retorna o id gerado pelo Mongo.
   */
  @Override
  public String startAsync(final JobType type) {
    final Instant now = Instant.now();
    final var expiration = now.plus(Duration.ofDays(7));

    // Se já existe um job desse tipo executando, ENFILEIRA
    if (runningFlag(type).get()) {
      final var queued = new JobInfoModel();
      queued.setExpiresAt(expiration);
      queued.setType(type);
      queued.setStatus(SyncStatus.QUEUED);
      queued.setMessage("queued");
      final var saved = jobRepo.save(queued);
      log.info("startAsync - tipo {} já em execução. Enfileirado jobId={}", type, saved.getId());
      return saved.getId();
    }

    // Caso contrário, cria RUNNING e inicia
    final var running = new JobInfoModel();
    running.setExpiresAt(expiration);
    running.setType(type);
    running.setStatus(SyncStatus.RUNNING);
    running.setStartedAt(now);
    running.setMessage("started");
    final var saved = jobRepo.save(running);
    runJobAsync(saved.getId(), type, /* fromQueue= */ false);
    return saved.getId();
  }

  /**
   * Varre a fila (QUEUED) e tenta iniciar jobs, respeitando o lock por tipo. Retorna quantos jobs
   * foram efetivamente iniciados.
   */
  @Override
  public int startQueuedJobs() {
    int started = 0;
    // Busque em lotes pequenos para não monopolizar a janela (ajuste conforme necessidade)
    final Pageable page = PageRequest.of(0, 50, Sort.by(Sort.Direction.ASC, "_id"));
    final List<JobInfoModel> queued = jobRepo.findByStatus(SyncStatus.QUEUED, page);

    for (final JobInfoModel j : queued) {
      final JobType type = j.getType();

      // Se já há job desse tipo rodando, pula (deixa na fila para próxima rodada)
      if (runningFlag(type).get()) {
        continue;
      }

      // Tenta "claimar": QUEUED -> RUNNING de forma atômica
      if (claimQueued(j.getId())) {
        runJobAsync(j.getId(), type, /* fromQueue= */ true);
        started++;
      }
    }
    return started;
  }

  /**
   * Método realmente assíncrono. Se não conseguir o lock por tipo: - fromQueue=true -> devolve a
   * QUEUED (continua aguardando) - fromQueue=false -> marca como REJECTED_ALREADY_RUNNING
   */
  @Async("syncExecutor")
  public void runJobAsync(final String jobId, final JobType type, final boolean fromQueue) {
    final var flag = runningFlag(type);
    if (!flag.compareAndSet(false, true)) {
      if (fromQueue) {
        // devolve pra fila
        updateFields(
            jobId,
            u -> {
              u.set("status", SyncStatus.QUEUED);
              u.set("message", "re-queued (tipo já executando)");
              u.unset("startedAt");
              u.unset("finishedAt");
            });
      } else {
        // disparo direto concorrente
        update(
            jobId,
            SyncStatus.REJECTED_ALREADY_RUNNING,
            Instant.now(),
            "Concorrência detectada ao iniciar o job");
      }
      return;
    }

    try {
      log.info("Iniciando job type={} jobId={}", type, jobId);
      switch (type) {
        case SINCRONIZAR_BASES -> arencoDatabaseSync.sincronizarBases();
        case SINCRONIZAR_BOLETOS -> boletosService.processarBoletos();
      }
      update(jobId, SyncStatus.DONE, Instant.now(), "ok");
      log.info("Job concluído type={} jobId={}", type, jobId);
    } catch (final Exception e) {
      log.error("Falha no job type={} jobId={}", type, jobId, e);
      update(jobId, SyncStatus.FAILED, Instant.now(), safeMessage(e.getMessage()));
    } finally {
      flag.set(false);
    }
  }

  @Override
  public boolean isRunning(final JobType type) {
    return runningFlag(type).get();
  }

  /* ================= helpers ================ */

  /** Tenta trocar QUEUED -> RUNNING com startedAt=now/mensagem; retorna true se conseguiu. */
  private boolean claimQueued(final String jobId) {
    final var now = Instant.now();
    final Query q = new Query(Criteria.where("_id").is(jobId).and("status").is(SyncStatus.QUEUED));
    final Update u =
        new Update()
            .set("status", SyncStatus.RUNNING)
            .set("startedAt", now)
            .set("message", "started (from queue)");
    final var opts = FindAndModifyOptions.options().returnNew(true);
    JobInfoModel updated = mongoTemplate.findAndModify(q, u, opts, JobInfoModel.class);
    return updated != null;
  }

  private void update(
      final String jobId,
      final SyncStatus newStatus,
      final Instant finishedAt,
      final String message) {

    jobRepo
        .findById(jobId)
        .ifPresent(
            j -> {
              j.setStatus(newStatus);
              j.setFinishedAt(finishedAt);
              j.setMessage(message);
              // preserva startedAt já existente
              jobRepo.save(j);
            });
  }

  /** Atualiza campos arbitrários via MongoTemplate (útil para re-queue). */
  private void updateFields(final String jobId, java.util.function.Consumer<Update> mutator) {
    final Query q = new Query(Criteria.where("_id").is(jobId));
    final Update u = new Update();
    mutator.accept(u);
    mongoTemplate.updateFirst(q, u, JobInfoModel.class);
  }

  private String safeMessage(String msg) {
    if (msg == null) return null;
    return msg.length() > 2000 ? msg.substring(0, 2000) : msg;
  }
}
