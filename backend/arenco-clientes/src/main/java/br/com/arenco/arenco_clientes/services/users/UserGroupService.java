package br.com.arenco.arenco_clientes.services.users;

import br.com.arenco.arenco_clientes.entities.UserGroupModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import java.util.List;

public interface UserGroupService {
  List<UserGroupModel> getGroupsByUser(final UserModel user);
}
