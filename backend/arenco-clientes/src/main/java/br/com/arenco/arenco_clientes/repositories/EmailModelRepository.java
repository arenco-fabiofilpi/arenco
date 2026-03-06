package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.EmailModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailModelRepository extends MongoRepository<EmailModel, String> {}
