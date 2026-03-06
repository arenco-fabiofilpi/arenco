package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.sync.JobInfoDto;
import br.com.arenco.arenco_clientes.enums.JobType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobInfoFacade {
  void createManualTrigger(final JobType jobType);

  Page<JobInfoDto> getAll(final Pageable pageable, final JobType jobType);
}
