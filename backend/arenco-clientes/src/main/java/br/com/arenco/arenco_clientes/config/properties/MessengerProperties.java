package br.com.arenco.arenco_clientes.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "arenco.messengerlib")
public class MessengerProperties {
  private String twilioAccountSid;
  private String twilioAuthToken;
  private String twilioFrom;
  private String sendgridApiKey;
  private String sendgridFrom;
}
