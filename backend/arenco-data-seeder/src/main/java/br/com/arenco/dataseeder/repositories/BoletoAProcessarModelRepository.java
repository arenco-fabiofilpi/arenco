package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.BoletoAProcessarModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoletoAProcessarModelRepository
    extends MongoRepository<BoletoAProcessarModel, String> {
}
