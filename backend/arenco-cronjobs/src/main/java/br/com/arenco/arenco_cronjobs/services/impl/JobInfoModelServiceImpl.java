package br.com.arenco.arenco_cronjobs.services.impl;

import br.com.arenco.arenco_cronjobs.entities.JobInfoModel;
import br.com.arenco.arenco_cronjobs.enums.JobType;
import br.com.arenco.arenco_cronjobs.enums.SyncStatus;
import br.com.arenco.arenco_cronjobs.repositories.JobInfoModelRepository;
import br.com.arenco.arenco_cronjobs.services.JobInfoModelService;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobInfoModelServiceImpl implements JobInfoModelService {
  private final JobInfoModelRepository jobRepo;

  @Override
  public void createManualTrigger(final JobType jobType, final String message) {
    final Instant now = Instant.now();
    final var expiration = now.plus(Duration.ofDays(7));
    final var queued = new JobInfoModel();
    queued.setExpiresAt(expiration);
    queued.setType(jobType);
    queued.setStatus(SyncStatus.QUEUED);
    queued.setMessage(message);
    final var saved = jobRepo.save(queued);
    log.info("Created job info model with id {}", saved.getId());
  }

  @Override
  public Page<@NonNull JobInfoModel> getAll(final Pageable pageable, final JobType jobType) {
    return jobRepo.findByType(jobType, pageable);
  }
}
