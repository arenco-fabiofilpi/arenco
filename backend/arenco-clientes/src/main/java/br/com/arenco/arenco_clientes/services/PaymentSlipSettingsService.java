package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.PaymentSlipSettingsModel;
import br.com.arenco.arenco_clientes.enums.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentSlipSettingsService {
  void save(final PaymentSlipSettingsModel model);

  void remove(final String id);

  PaymentSlipSettingsModel getPaymentSlipSettings(final String id);

  Page<PaymentSlipSettingsModel> findAll(final Pageable pageable);

  boolean existsByBank(final Bank bank);
}
