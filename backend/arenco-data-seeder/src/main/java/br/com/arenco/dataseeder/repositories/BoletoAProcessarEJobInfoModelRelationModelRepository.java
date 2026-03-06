package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.BoletoAProcessarEJobInfoModelRelationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoletoAProcessarEJobInfoModelRelationModelRepository
    extends MongoRepository<BoletoAProcessarEJobInfoModelRelationModel, String> {}
