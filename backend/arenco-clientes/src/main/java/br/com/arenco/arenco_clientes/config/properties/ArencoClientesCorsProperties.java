package br.com.arenco.arenco_clientes.config.properties;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "arenco.cors")
@Data
public class ArencoClientesCorsProperties {
  private List<String> corsAllowedOrigins;
  private List<String> corsAllowedMethods;
  private List<String> corsAllowedHeaders;
}
