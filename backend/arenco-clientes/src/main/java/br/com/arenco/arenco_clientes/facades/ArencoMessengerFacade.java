package br.com.arenco.arenco_clientes.facades;

import br.com.arenco.arenco_clientes.dtos.EmailDto;
import br.com.arenco.arenco_clientes.dtos.SmsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArencoMessengerFacade {

  Page<SmsDto> findAllSentSms(final Pageable pageable);

  Page<EmailDto> findAllSentEmails(final Pageable pageable);
}
