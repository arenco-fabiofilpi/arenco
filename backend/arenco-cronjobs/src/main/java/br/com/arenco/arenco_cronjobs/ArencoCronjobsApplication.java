package br.com.arenco.arenco_cronjobs;

import br.com.arenco.arenco_cronjobs.enums.JobType;
import br.com.arenco.arenco_cronjobs.orchestrator.SyncOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@EnableScheduling
@SpringBootApplication
@RequiredArgsConstructor
public class ArencoCronjobsApplication {
  public static void main(String[] args) {
    SpringApplication.run(ArencoCronjobsApplication.class, args);
  }

  private final SyncOrchestrator orchestrator;

  @Scheduled(cron = "0 0 8-20/4 * * *", zone = "America/Sao_Paulo")
  public void sincronizarBases() {
    log.info("sincronizarBases - Iniciando execução");

    if (orchestrator.isRunning(JobType.SINCRONIZAR_BASES)) {
      log.error("sincronizarBases - Já existe uma sincronização em andamento");
      return;
    }

    final var id = orchestrator.startAsync(JobType.SINCRONIZAR_BASES);

    log.info("sincronizarBases - ✅ Execução iniciada com sucesso. ID: {}", id);
  }

  @Scheduled(fixedDelay = 15_000)
  public void checarSincronizacaoManual() {
    log.debug("checarSincronizacaoManual - Iniciando execucao");
    final var i = orchestrator.startQueuedJobs();
    if (i > 0) {
      log.info("checarSincronizacaoManual - ✅ Execução finalizada. Iniciada(s) {} cronjob(s)", i);
    }
  }
}
