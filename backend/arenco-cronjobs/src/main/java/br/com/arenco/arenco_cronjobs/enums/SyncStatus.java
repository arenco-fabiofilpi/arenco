package br.com.arenco.arenco_cronjobs.enums;

public enum SyncStatus {
  QUEUED,
  RUNNING,
  DONE,
  REJECTED_ALREADY_RUNNING,
  FAILED
}
