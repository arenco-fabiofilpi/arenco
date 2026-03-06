package br.com.arenco.arenco_clientes.strategies.impl;

import br.com.arenco.arenco_clientes.config.properties.MessengerProperties;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.enums.ContactType;
import br.com.arenco.arenco_clientes.services.ArencoEmailService;
import br.com.arenco.arenco_clientes.strategies.SendOtpStrategy;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.time.Year;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendOtpUsingEmailStrategy implements SendOtpStrategy {
  private final MessengerProperties messengerProperties;
  private final ArencoEmailService arencoEmailService;

  @Override
  public boolean apply(final ContactModel contact) {
    return contact.getContactType() == ContactType.EMAIL;
  }

  @Override
  public void sendToken(final String name, final String to, final String token) {
    log.debug("Sending password recovery email to {}", to);
    final var toObject = new Email(to);
    final Personalization personalization = populateEmailInformation(name, token, toObject);
    final String templateId = "d-97c24ac6cc744c02ae3602b43bfdb68c";
    arencoEmailService.send(
        toObject,
        new Email(messengerProperties.getSendgridFrom()),
        "Arenco - OTP",
        templateId,
        List.of(personalization));
  }

  private Personalization populateEmailInformation(
      final String name, final String token, final Email to) {
    final Personalization personalization = new Personalization();
    personalization.addTo(to);
    personalization.addDynamicTemplateData("nome", name);
    personalization.addDynamicTemplateData("codigo", token);
    personalization.addDynamicTemplateData("ano", String.valueOf(Year.now().getValue()));
    return personalization;
  }
}
