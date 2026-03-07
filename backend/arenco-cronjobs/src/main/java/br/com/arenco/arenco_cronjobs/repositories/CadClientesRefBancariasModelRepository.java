package br.com.arenco.arenco_cronjobs.repositories;


import br.com.arenco.arenco_cronjobs.entities.CadClientesRefBancariasModel;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CadClientesRefBancariasModelRepository extends MongoRepository<@NonNull CadClientesRefBancariasModel, @NonNull String> {
}
