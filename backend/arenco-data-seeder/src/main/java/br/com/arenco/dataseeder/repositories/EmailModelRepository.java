package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.EmailModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailModelRepository extends MongoRepository<EmailModel, String> {}
