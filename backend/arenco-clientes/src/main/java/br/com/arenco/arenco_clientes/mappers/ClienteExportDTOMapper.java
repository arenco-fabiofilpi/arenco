package br.com.arenco.arenco_clientes.mappers;

import br.com.arenco.arenco_clientes.dtos.ClienteExportDTO;
import br.com.arenco.arenco_clientes.entities.UserModel;

public record ClienteExportDTOMapper(UserModel userModel) {
  public ClienteExportDTO create() {
    final ClienteExportDTO dto = new ClienteExportDTO();
    return dto;
  }
}
