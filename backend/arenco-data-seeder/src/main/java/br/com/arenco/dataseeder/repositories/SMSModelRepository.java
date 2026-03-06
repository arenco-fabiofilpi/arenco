package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.SMSModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SMSModelRepository extends MongoRepository<SMSModel, String> {}
