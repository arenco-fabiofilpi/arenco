package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.MessengerSettingsModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessengerSettingsModelRepository
    extends MongoRepository<MessengerSettingsModel, String> {}
