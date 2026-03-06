package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.EmailModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailModelRepository extends MongoRepository<EmailModel, String> {}
