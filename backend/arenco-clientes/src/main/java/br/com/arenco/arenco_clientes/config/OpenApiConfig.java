package br.com.arenco.arenco_clientes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Arenco Clientes Swagger")
                .version("1.0")
                .description("Swagger de integrações Aplicação Arenco Clientes Spring Boot"));
  }
}
