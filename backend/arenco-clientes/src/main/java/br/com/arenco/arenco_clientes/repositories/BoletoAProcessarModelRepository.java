package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.BoletoAProcessarModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoletoAProcessarModelRepository
    extends MongoRepository<BoletoAProcessarModel, String> {
}
