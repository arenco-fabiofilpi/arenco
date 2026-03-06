package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.PasswordRecoveryOtpModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PasswordRecoveryOtpModelRepository
    extends MongoRepository<PasswordRecoveryOtpModel, String> {}
