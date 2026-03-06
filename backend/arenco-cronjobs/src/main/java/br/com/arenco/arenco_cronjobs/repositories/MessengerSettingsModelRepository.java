package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.MessengerSettingsModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessengerSettingsModelRepository extends MongoRepository<MessengerSettingsModel, String> {}
