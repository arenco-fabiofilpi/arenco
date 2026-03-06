package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.PasswordRecoveryOtpModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PasswordRecoveryOtpModelRepository
    extends MongoRepository<PasswordRecoveryOtpModel, String> {}
