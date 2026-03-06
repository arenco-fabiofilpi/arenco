package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.BoletoAProcessarEJobInfoModelRelationModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoletoAProcessarEJobInfoModelRelationModelRepository
    extends MongoRepository<BoletoAProcessarEJobInfoModelRelationModel, String> {}
