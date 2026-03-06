package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.user.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUsersFacade {
  Page<UserDto> findAll(final Pageable pageable);

  void removeAdminUser(final String id);
}
