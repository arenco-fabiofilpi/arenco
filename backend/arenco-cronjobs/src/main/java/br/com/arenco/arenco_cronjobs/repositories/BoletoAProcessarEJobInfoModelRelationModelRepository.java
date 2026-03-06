package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.BoletoAProcessarEJobInfoModelRelationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoletoAProcessarEJobInfoModelRelationModelRepository
    extends MongoRepository<BoletoAProcessarEJobInfoModelRelationModel, String> {}
