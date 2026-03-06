package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.UserModel;
import java.util.Optional;

public interface ArencoAuthLibUserService {
  Optional<UserModel> findById(final String id);
}
