package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.SessionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionModelRepository extends MongoRepository<SessionModel, String> {
}
