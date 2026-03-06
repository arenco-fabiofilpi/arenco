package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.SMSModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SMSModelRepository extends MongoRepository<SMSModel, String> {}
