package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.BetaUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BetaUserModelRepository extends MongoRepository<BetaUserModel, String> {
    boolean existsByUserModelId(final String userModelId);
}
