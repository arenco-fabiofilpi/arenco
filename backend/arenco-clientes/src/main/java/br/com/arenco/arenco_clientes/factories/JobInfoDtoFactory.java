package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.sync.JobInfoDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.entities.JobInfoModel;

public record JobInfoDtoFactory(JobInfoModel model) {
  public JobInfoDto create() {
    final var dto = new JobInfoDto();
    dto.setId(model.getId());
    dto.setDateCreated(ArencoDateUtils.fromInstantToString(model.getDateCreated()));
    dto.setLastUpdated(ArencoDateUtils.fromInstantToString(model.getLastUpdated()));
    dto.setType(model.getType().toString());
    dto.setStatus(model.getStatus().toString());
    dto.setStartedAt(ArencoDateUtils.fromInstantToString(model.getStartedAt()));
    dto.setFinishedAt(ArencoDateUtils.fromInstantToString(model.getFinishedAt()));
    dto.setMessage(model.getMessage());
    return dto;
  }
}
