package br.com.arenco.arenco_cronjobs.repositories;


import br.com.arenco.arenco_cronjobs.entities.CadTipoClienteModel;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CadTipoClienteModelRepository extends MongoRepository<@NonNull CadTipoClienteModel, @NonNull String> {}
