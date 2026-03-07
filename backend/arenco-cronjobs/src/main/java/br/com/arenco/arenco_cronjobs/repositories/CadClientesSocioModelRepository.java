package br.com.arenco.arenco_cronjobs.repositories;


import br.com.arenco.arenco_cronjobs.entities.CadClientesSocioModel;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CadClientesSocioModelRepository extends MongoRepository<@NonNull CadClientesSocioModel, @NonNull String> {}
