package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.SessionModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionModelRepository extends MongoRepository<SessionModel, String> {}
