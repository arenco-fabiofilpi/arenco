package br.com.arenco.arenco_cronjobs.repositories;

import br.com.arenco.arenco_cronjobs.entities.PaymentSlipSettingsModel;
import br.com.arenco.arenco_cronjobs.enums.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentSlipSettingsModelRepository
    extends MongoRepository<PaymentSlipSettingsModel, String> {

  boolean existsByBanco(final Bank banco);
}
