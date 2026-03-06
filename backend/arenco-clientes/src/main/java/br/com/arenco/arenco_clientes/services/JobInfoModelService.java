package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.JobInfoModel;
import br.com.arenco.arenco_clientes.enums.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobInfoModelService {
  void createManualTrigger(final JobType jobType, final String message);

  Page<JobInfoModel> getAll(final Pageable pageable, final JobType jobType);
}
