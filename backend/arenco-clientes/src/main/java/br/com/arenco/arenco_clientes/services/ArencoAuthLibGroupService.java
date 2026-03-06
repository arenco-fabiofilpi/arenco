package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.RoleModel;
import br.com.arenco.arenco_clientes.entities.UserGroupModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

public interface ArencoAuthLibGroupService {
  List<UserGroupModel> getGroupsByUser(final UserModel user);

  List<RoleModel> getRolesByGroupList(final List<UserGroupModel> groupModelList);

  Collection<? extends GrantedAuthority> getGrantedAuthorities(
      final List<UserGroupModel> userGroups);
}
