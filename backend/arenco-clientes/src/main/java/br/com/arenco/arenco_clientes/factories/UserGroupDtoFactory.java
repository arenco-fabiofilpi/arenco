package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.user.UserGroupDto;
import br.com.arenco.arenco_clientes.entities.UserGroupModel;

public record UserGroupDtoFactory(UserGroupModel model) {
  public UserGroupDto create() {
    final var dto = new UserGroupDto();
    dto.setName(model.getName());
    dto.setCode(model.getCode());
    dto.setId(model.getId());
    return dto;
  }
}
