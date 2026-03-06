package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.PasswordRecoveryOtpModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PasswordRecoveryOtpModelRepository
    extends MongoRepository<PasswordRecoveryOtpModel, String> {}
