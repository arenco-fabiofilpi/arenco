package br.com.arenco.dataseeder.repositories;

import br.com.arenco.dataseeder.entities.PaymentSlipSettingsModel;
import br.com.arenco.dataseeder.enums.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentSlipSettingsModelRepository
    extends MongoRepository<PaymentSlipSettingsModel, String> {

  boolean existsByBanco(final Bank banco);
}
