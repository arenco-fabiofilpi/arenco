package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.user.ContactDto;
import br.com.arenco.arenco_clientes.dtos.user.UserDto;
import br.com.arenco.arenco_clientes.dtos.user.UserGroupDto;
import br.com.arenco.arenco_clientes.entities.UserModel;
import java.util.List;
import java.util.Optional;

public interface UsersFacade {
  UserModel findUser(final String id);

  Optional<UserDto> getCurrentUser();

  List<ContactDto> getCurrentUserContacts();

  List<UserGroupDto> getCurrentUserGroups();

  void changeCurrentUserPassword(final String oldPassword, final String newPassword);
}
