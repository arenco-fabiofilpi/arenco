package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUsersService {
  Page<UserModel> findAllAdminUsers(final Pageable pageable);

  UserModel findAdminUser(final String id);

  void removeAdminUser(final UserModel user);
}
