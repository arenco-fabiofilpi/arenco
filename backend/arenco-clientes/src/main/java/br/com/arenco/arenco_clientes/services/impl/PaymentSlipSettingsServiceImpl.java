package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.PaymentSlipSettingsService;
import br.com.arenco.arenco_clientes.exceptions.IdNotFoundException;
import br.com.arenco.arenco_clientes.entities.PaymentSlipSettingsModel;
import br.com.arenco.arenco_clientes.enums.Bank;
import br.com.arenco.arenco_clientes.repositories.PaymentSlipSettingsModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentSlipSettingsServiceImpl implements PaymentSlipSettingsService {
  private final PaymentSlipSettingsModelRepository repository;

  @Override
  public void save(final PaymentSlipSettingsModel model) {
    repository.save(model);
  }

  @Override
  public void remove(final String id) {
    final var model = getPaymentSlipSettings(id);
    repository.delete(model);
    log.info("Removed payment slip settings with id {}", id);
  }

  @Override
  public PaymentSlipSettingsModel getPaymentSlipSettings(final String id) {
    return repository
        .findById(id)
        .orElseThrow(
            () ->
                new IdNotFoundException("Nao encontrado Configuração de Pagamento para ID " + id));
  }

  @Override
  public Page<PaymentSlipSettingsModel> findAll(final Pageable pageable) {
    return repository.findAll(pageable);
  }

  @Override
  public boolean existsByBank(final Bank bank) {
    return repository.existsByBanco(bank);
  }
}
