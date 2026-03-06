package br.com.arenco.arenco_clientes.repositories;

import br.com.arenco.arenco_clientes.entities.PaymentSlipSettingsModel;
import br.com.arenco.arenco_clientes.enums.Bank;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentSlipSettingsModelRepository
    extends MongoRepository<PaymentSlipSettingsModel, String> {

  boolean existsByBanco(final Bank banco);
}
