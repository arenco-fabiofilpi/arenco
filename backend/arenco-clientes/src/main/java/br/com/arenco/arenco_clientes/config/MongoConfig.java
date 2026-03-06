package br.com.arenco.arenco_clientes.config;

import java.time.Instant;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
@EnableMongoRepositories(basePackages = {"br.com.arenco.arenco_clientes.repositories"})
public class MongoConfig {
  @Bean
  public DateTimeProvider auditingDateTimeProvider() {
    // Instant.now() -> será convertido para Date/Instant/LocalDateTime conforme seu campo
    return () -> Optional.of(Instant.now());
  }
}
