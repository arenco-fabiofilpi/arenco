package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.services.ArencoEmailService;
import br.com.arenco.arenco_clientes.services.ArencoSmsService;
import br.com.arenco.arenco_clientes.dtos.EmailDto;
import br.com.arenco.arenco_clientes.dtos.SmsDto;
import br.com.arenco.arenco_clientes.facades.ArencoMessengerFacade;
import br.com.arenco.arenco_clientes.factories.EmailDtoFactory;
import br.com.arenco.arenco_clientes.factories.SmsDtoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArencoMessengerFacadeImpl implements ArencoMessengerFacade {
  private final ArencoSmsService arencoSmsService;
  private final ArencoEmailService arencoEmailService;

  @Override
  public Page<SmsDto> findAllSentSms(final Pageable pageable) {
    final var modelPage = arencoSmsService.findAll(pageable);
    return modelPage.map(model -> new SmsDtoFactory(model).create());
  }

  @Override
  public Page<EmailDto> findAllSentEmails(final Pageable pageable) {
    final var modelPage = arencoEmailService.findAll(pageable);
    return modelPage.map(model -> new EmailDtoFactory(model).create());
  }
}
