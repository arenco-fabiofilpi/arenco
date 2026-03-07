package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.CadClientesOutrosModel;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CadClientesOutrosModelRepository
    extends MongoRepository<@NonNull CadClientesOutrosModel, @NonNull String> {}
