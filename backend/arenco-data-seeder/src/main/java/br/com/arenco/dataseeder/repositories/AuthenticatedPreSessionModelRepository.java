package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.AuthenticatedPreSessionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthenticatedPreSessionModelRepository
    extends MongoRepository<AuthenticatedPreSessionModel, String> {}
