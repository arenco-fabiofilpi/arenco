package br.com.arenco.arenco_cronjobs.repositories;


import br.com.arenco.arenco_cronjobs.entities.CadClientesRefComerciaisModel;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CadClientesRefComerciaisModelRepository extends MongoRepository<@NonNull CadClientesRefComerciaisModel, @NonNull String> {
}
