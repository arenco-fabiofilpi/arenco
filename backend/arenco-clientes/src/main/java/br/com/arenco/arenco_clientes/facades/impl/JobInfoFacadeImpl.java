package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.facades.JobInfoFacade;
import br.com.arenco.arenco_clientes.dtos.sync.JobInfoDto;
import br.com.arenco.arenco_clientes.factories.JobInfoDtoFactory;
import br.com.arenco.arenco_clientes.enums.JobType;
import br.com.arenco.arenco_clientes.services.JobInfoModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobInfoFacadeImpl implements JobInfoFacade {
  private final JobInfoModelService jobInfoModelService;

  @Override
  public void createManualTrigger(final JobType jobType) {
    jobInfoModelService.createManualTrigger(jobType, "Solicitado pelo site");
  }

  @Override
  public Page<JobInfoDto> getAll(final Pageable pageable, final JobType jobType) {
    final var models = jobInfoModelService.getAll(pageable, jobType);
    return models.map(i -> new JobInfoDtoFactory(i).create());
  }
}
