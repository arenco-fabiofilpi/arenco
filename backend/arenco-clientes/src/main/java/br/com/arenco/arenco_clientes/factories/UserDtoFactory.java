package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.user.UserDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.entities.UserModel;

public record UserDtoFactory(UserModel model) {
  public UserDto create() {
    final var dto = new UserDto();
    populate(dto);
    return dto;
  }

  public void populate(UserDto dto) {
    dto.setCreatedAt(ArencoDateUtils.fromInstantToString(model.getDateCreated()));
    dto.setUpdatedAt(ArencoDateUtils.fromInstantToString(model.getLastUpdated()));
    dto.setId(model.getId());
    dto.setName(model.getName());
    dto.setEnabled(String.valueOf(model.isEnabled()));
    dto.setLoginMethod(model.getLoginMethod().toString());
    dto.setUsername(model.getUsername());
    dto.setCnpj(model.getCnpj());
    dto.setCpf(model.getCpf());
    dto.setIdErp(model.getIdErp());
    dto.setGrupoContabil(model.getGrupoContabil());
  }
}
