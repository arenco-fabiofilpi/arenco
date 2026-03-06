package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.SMSModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SMSModelRepository extends MongoRepository<SMSModel, String> {}
