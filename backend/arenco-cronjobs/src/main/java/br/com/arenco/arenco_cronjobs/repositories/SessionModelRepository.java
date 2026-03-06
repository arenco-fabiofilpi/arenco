package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.SessionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionModelRepository extends MongoRepository<SessionModel, String> {
}
