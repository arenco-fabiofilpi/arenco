package br.com.arenco.arenco_cronjobs.orchestrator;

import br.com.arenco.arenco_cronjobs.enums.JobType;

public interface SyncOrchestrator {
  int startQueuedJobs();

  String startAsync(final JobType type);

  boolean isRunning(final JobType type);
}
