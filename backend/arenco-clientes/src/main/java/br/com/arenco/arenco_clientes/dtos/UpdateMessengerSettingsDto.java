package br.com.arenco.arenco_clientes.dtos;

import br.com.arenco.arenco_clientes.enums.AutomaticPaymentSendState;
import br.com.arenco.arenco_clientes.enums.AutomaticPaymentSendStrategy;
import br.com.arenco.arenco_clientes.enums.AutomaticPaymentTargetUsers;

public record UpdateMessengerSettingsDto(
    AutomaticPaymentSendState state,
    AutomaticPaymentSendStrategy strategy,
    AutomaticPaymentTargetUsers targetUsers) {}
