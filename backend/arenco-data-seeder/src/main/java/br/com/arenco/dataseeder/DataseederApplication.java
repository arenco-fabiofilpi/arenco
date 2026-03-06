package br.com.arenco.dataseeder;

import br.com.arenco.dataseeder.services.DataSeederService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/** Classe responsável por realizar o seed inicial de dados a partir do arquivo data.json. */
@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class DataseederApplication implements CommandLineRunner {
  private final DataSeederService service;
  private final ApplicationContext context;

  public static void main(String[] args) {
    SpringApplication.run(DataseederApplication.class, args);
  }

  @Override
  public void run(final String... args) {
    try {
      service.seed();
    } catch (Exception e) {
      log.error("Erro ao executar seed: {}", e.getMessage(), e);
      // retorna código de erro 1
      System.exit(SpringApplication.exit(context, () -> 1));
    }

    // encerra normalmente com código 0
    System.exit(SpringApplication.exit(context, () -> 0));
  }
}
