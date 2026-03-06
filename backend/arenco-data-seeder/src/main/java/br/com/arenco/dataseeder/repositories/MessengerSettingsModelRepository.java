package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.MessengerSettingsModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessengerSettingsModelRepository extends MongoRepository<MessengerSettingsModel, String> {}
