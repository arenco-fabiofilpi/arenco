package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.user.ContactPreferenceDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.entities.ContactPreferenceModel;
import br.com.arenco.arenco_clientes.entities.UserModel;

public record ContactPreferenceDtoFactory(
    ContactPreferenceModel contactPreferenceModel, ContactModel contactModel, UserModel userModel) {
  public ContactPreferenceDto create() {
    final ContactPreferenceDto dto = new ContactPreferenceDto();

    dto.setUserFullName(userModel.getName());
    dto.setCreatedAt(ArencoDateUtils.fromInstantToString(contactPreferenceModel.getDateCreated()));
    dto.setUpdatedAt(ArencoDateUtils.fromInstantToString(contactPreferenceModel.getLastUpdated()));
    dto.setValue(contactModel.getValue());
    dto.setContactType(contactModel.getContactType());
    dto.setActive(contactPreferenceModel.isActive());
    dto.setOwnerIdErp(userModel.getIdErp());

    return dto;
  }
}
