package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.AuthenticatedPreSessionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthenticatedPreSessionModelRepository
    extends MongoRepository<AuthenticatedPreSessionModel, String> {}
