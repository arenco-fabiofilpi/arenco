package br.com.arenco.arenco_clientes.services.users;

import br.com.arenco.arenco_clientes.entities.UserModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
  Optional<UserModel> findUserById(final String id);

  UserModel findUserById(final String id, Class<? extends Exception> exceptionClass);

  void save(final UserModel user);

  Page<UserModel> findAllCustomers(final Pageable pageable);

  List<UserModel> findAllCustomers(final List<String> ids);
}
