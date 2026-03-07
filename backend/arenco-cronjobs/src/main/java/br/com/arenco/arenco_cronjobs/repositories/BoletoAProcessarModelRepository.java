package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.BoletoAProcessarModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoletoAProcessarModelRepository
    extends MongoRepository<BoletoAProcessarModel, String> {}
