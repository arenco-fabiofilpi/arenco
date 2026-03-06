package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.LoginOtpModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LoginOtpModelRepository extends MongoRepository<LoginOtpModel, String> {
  Optional<LoginOtpModel> findFirstByUserModelId(final String userModelId);
}
