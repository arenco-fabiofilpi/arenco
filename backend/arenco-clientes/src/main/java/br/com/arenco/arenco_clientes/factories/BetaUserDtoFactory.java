package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.settings.BetaUserDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.entities.BetaUserModel;
import br.com.arenco.arenco_clientes.entities.UserModel;

public record BetaUserDtoFactory(BetaUserModel betaUserModel, UserModel userModel) {
  public BetaUserDto create() {
    final BetaUserDto dto = new BetaUserDto();
    dto.setId(betaUserModel.getId());
    dto.setCreatedAt(ArencoDateUtils.fromInstantToString(betaUserModel.getDateCreated()));
    dto.setUpdatedAt(ArencoDateUtils.fromInstantToString(betaUserModel.getLastUpdated()));
    setUserModelData(dto);
    return dto;
  }

  private void setUserModelData(BetaUserDto dto) {
    if (userModel != null) {
      dto.setName(userModel().getName());
      dto.setIdErp(userModel.getIdErp());
      dto.setUsername(userModel.getUsername());
    }
  }
}
