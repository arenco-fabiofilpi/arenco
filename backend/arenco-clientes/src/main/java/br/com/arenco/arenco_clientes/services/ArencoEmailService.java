package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.EmailModel;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArencoEmailService {
  void send(
      final Email to,
      final Email from,
      final String subject,
      final String templateId,
      final List<Personalization> personalizations);

  Page<EmailModel> findAll(final Pageable pageable);
}
