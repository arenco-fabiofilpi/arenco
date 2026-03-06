package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.SMSModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArencoSmsService {
  void sendSms(final String to, final String message, final String from);

  void registerTwilioAccount();

  Page<SMSModel> findAll(final Pageable pageable);
}
