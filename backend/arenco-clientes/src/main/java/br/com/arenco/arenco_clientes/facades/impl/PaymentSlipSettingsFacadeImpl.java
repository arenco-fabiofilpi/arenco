package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.exceptions.ConfiguracaoDeBoletoJaExistente;
import br.com.arenco.arenco_clientes.facades.PaymentSlipSettingsFacade;
import br.com.arenco.arenco_clientes.mappers.PaymentSlipSettingsMapper;
import br.com.arenco.arenco_clientes.services.PaymentSlipSettingsService;
import br.com.arenco.arenco_clientes.dtos.settings.PaymentSlipSettingsDto;
import br.com.arenco.arenco_clientes.factories.PaymentSlipSettingsDtoFactory;
import br.com.arenco.arenco_clientes.entities.PaymentSlipSettingsModel;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSlipSettingsFacadeImpl implements PaymentSlipSettingsFacade {
  private final PaymentSlipSettingsService paymentSlipSettingsService;

  @Override
  public Page<PaymentSlipSettingsDto> getListOfPaymentSlipSettings(final Pageable pageable) {
    final var models = paymentSlipSettingsService.findAll(pageable);
    return models.map(i -> new PaymentSlipSettingsDtoFactory(i).create());
  }

  @Override
  public PaymentSlipSettingsDto getPaymentSlipSettings(final String id) {
    final var model = paymentSlipSettingsService.getPaymentSlipSettings(id);
    return new PaymentSlipSettingsDtoFactory(model).create();
  }

  @Override
  public URI createPaymentSlipSettings(final PaymentSlipSettingsDto dto) {
    final var existente = paymentSlipSettingsService.existsByBank(dto.getBanco());
    if (existente) {
      throw new ConfiguracaoDeBoletoJaExistente(
          "Configuração de Boleto já existente para o Banco " + dto.getBanco());
    }
    final var model = new PaymentSlipSettingsModel();
    PaymentSlipSettingsMapper.updateModelFromDto(dto, model);
    paymentSlipSettingsService.save(model);
    log.info("createPaymentSlipSettings - Created model: {}", model.getId());
    return URI.create("/admin/payment-slip-settings/" + model.getId());
  }

  @Override
  public void updatePaymentSlipSettings(final String id, final PaymentSlipSettingsDto dto) {
    final var model = paymentSlipSettingsService.getPaymentSlipSettings(dto.getId());
    PaymentSlipSettingsMapper.updateModelFromDto(dto, model);
    paymentSlipSettingsService.save(model);
    log.info("updatePaymentSlipSettings - Updated model: {}", model.getId());
  }

  @Override
  public void deletePaymentSlipSettings(final String id) {
    paymentSlipSettingsService.remove(id);
  }
}
