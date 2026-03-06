package br.com.arenco.arenco_cronjobs.services;

import br.com.arenco.arenco_cronjobs.entities.JobInfoModel;
import br.com.arenco.arenco_cronjobs.enums.JobType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobInfoModelService {
  void createManualTrigger(final JobType jobType, final String message);

  Page<@NonNull JobInfoModel> getAll(final Pageable pageable, final JobType jobType);
}
