package br.com.arenco.arenco_cronjobs.entities;

import br.com.arenco.arenco_cronjobs.enums.AutomaticPaymentSendState;
import br.com.arenco.arenco_cronjobs.enums.AutomaticPaymentSendStrategy;
import br.com.arenco.arenco_cronjobs.enums.AutomaticPaymentTargetUsers;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document
public class MessengerSettingsModel {
  public static final String ID = "messenger-settings";
  @Id private String id = ID;
  @Version
  private Long version;

  // Configuracoes para envio automatico de boletos
  private AutomaticPaymentSendState state;
  private AutomaticPaymentSendStrategy strategy;
  private AutomaticPaymentTargetUsers targetUsers;
}
