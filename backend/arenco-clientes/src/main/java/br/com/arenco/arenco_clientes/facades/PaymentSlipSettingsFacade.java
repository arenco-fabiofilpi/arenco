package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.settings.PaymentSlipSettingsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;

public interface PaymentSlipSettingsFacade {
  Page<PaymentSlipSettingsDto> getListOfPaymentSlipSettings(final Pageable pageable);

  PaymentSlipSettingsDto getPaymentSlipSettings(final String id);

  URI createPaymentSlipSettings(final PaymentSlipSettingsDto dto);

  void deletePaymentSlipSettings(final String id);

  void updatePaymentSlipSettings(final String id, final PaymentSlipSettingsDto dto);
}
