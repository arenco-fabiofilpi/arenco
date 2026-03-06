package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.enums.UserType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserModelRepository extends MongoRepository<UserModel, String> {
  Optional<UserModel> findByUsername(final String username);

  boolean existsByUsername(final String username);

  Optional<UserModel> findFirstByIdErp(final int idErp);

  Page<UserModel> findAllByUserType(final UserType userType, final Pageable pageable);

  List<UserModel> findAllById(final List<String> ids);

  Optional<UserModel> findByIdAndUserType(final String id, final UserType userType);
}
