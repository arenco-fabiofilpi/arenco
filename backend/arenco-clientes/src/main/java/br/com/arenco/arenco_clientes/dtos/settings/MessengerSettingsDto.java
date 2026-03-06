package br.com.arenco.arenco_clientes.dtos.settings;

import br.com.arenco.arenco_clientes.enums.AutomaticPaymentSendState;
import br.com.arenco.arenco_clientes.enums.AutomaticPaymentSendStrategy;
import br.com.arenco.arenco_clientes.enums.AutomaticPaymentTargetUsers;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessengerSettingsDto {
  private String id;
  private Long version;
  private AutomaticPaymentSendState state;
  private AutomaticPaymentSendStrategy strategy;
  private AutomaticPaymentTargetUsers targetUsers;
}
