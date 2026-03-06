package br.com.arenco.arenco_clientes.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "arenco.authlib")
public class AuthenticationProperties {
  private String sessionPepper;
  private String preSessionPepper;
  private boolean localEnvironment = false;
}
