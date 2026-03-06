package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.user.ContactFullDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.entities.UserModel;

public record ContactFullDtoFactory(ContactModel contactModel, UserModel userModel) {
  public ContactFullDto create() {
    final ContactFullDto dto = new ContactFullDto();

    dto.setCreatedAt(ArencoDateUtils.fromInstantToString(contactModel.getDateCreated()));
    dto.setUpdatedAt(ArencoDateUtils.fromInstantToString(contactModel.getLastUpdated()));
    dto.setUserFullName(userModel.getName());
    dto.setValue(contactModel.getValue());
    dto.setContactType(contactModel.getContactType());
    dto.setActive(contactModel.isActive());
    dto.setOwnerIdErp(userModel.getIdErp());

    return dto;
  }
}
