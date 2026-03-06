package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.AuthenticatedPreSessionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthenticatedPreSessionModelRepository
    extends MongoRepository<AuthenticatedPreSessionModel, String> {}
