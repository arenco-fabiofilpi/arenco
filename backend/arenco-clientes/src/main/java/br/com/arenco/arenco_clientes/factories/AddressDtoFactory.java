package br.com.arenco.arenco_clientes.factories;

import br.com.arenco.arenco_clientes.dtos.user.AddressDto;
import br.com.arenco.arenco_clientes.utils.ArencoDateUtils;
import br.com.arenco.arenco_clientes.entities.AddressModel;
import br.com.arenco.arenco_clientes.entities.UserModel;

public record AddressDtoFactory(AddressModel addressModel, UserModel userModel) {
  public AddressDto create() {
    final AddressDto dto = new AddressDto();
    dto.setUserFullName(userModel.getName());
    dto.setOwnerIdErp(userModel.getIdErp());
    dto.setCreatedAt(ArencoDateUtils.fromInstantToString(addressModel.getDateCreated()));
    dto.setUpdatedAt(ArencoDateUtils.fromInstantToString(addressModel.getLastUpdated()));
    dto.setStreetNumber(addressModel.getStreetNumber());
    dto.setStreetName(addressModel.getStreetName());
    dto.setCity(addressModel.getCity());
    dto.setState(addressModel.getState());
    dto.setCountry(addressModel.getCountry());
    dto.setCep(addressModel.getCep());
    dto.setDistrict(addressModel.getDistrict());
    return dto;
  }
}
