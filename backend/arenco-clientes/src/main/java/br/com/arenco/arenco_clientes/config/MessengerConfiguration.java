package br.com.arenco.arenco_clientes.config;

import br.com.arenco.arenco_clientes.config.properties.MessengerProperties;
import com.sendgrid.SendGrid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessengerConfiguration {
    @Bean
    public SendGrid sendgrid(final MessengerProperties properties) {
        return new SendGrid(properties.getSendgridApiKey());
    }
}
