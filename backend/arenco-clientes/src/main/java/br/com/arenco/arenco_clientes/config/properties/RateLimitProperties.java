package br.com.arenco.arenco_clientes.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "ratelimit")
public class RateLimitProperties {
  private int capacity = 100;
  private int refillTokens = 100;
  private String refillPeriod = "1m";
  private String includePaths = "/api/**";
}
