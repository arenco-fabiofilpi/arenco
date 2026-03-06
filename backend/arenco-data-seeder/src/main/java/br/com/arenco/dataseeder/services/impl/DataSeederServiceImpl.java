package br.com.arenco.dataseeder.services.impl;

import br.com.arenco.dataseeder.services.DataSeederService;
import br.com.arenco.dataseeder.strategies.DataSeederStrategy;
import br.com.arenco.dataseeder.strategies.DataSeederStrategyList;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSeederServiceImpl implements DataSeederService {
  private final DataSeederStrategyList dataSeederStrategyList;

  @Override
  public void seed() throws IOException {
    final InputStream is = getClass().getResourceAsStream("/data.json");
    if (is == null) {
      log.warn("Arquivo data.json não encontrado no classpath. Ignorando DataSeeder.");
      return;
    }

    final JsonNode rootNode = new ObjectMapper().readTree(is);
    for (final JsonNode collectionNode : rootNode) {
      final String collection = collectionNode.path("collection").asText();
      final JsonNode documents = collectionNode.path("documents");

      log.info("Processando coleção '{}', total de {} documentos...", collection, documents.size());

      seedCollection(collection, documents);
    }

    log.info("DataSeeder concluído com sucesso.");
  }

  private void seedCollection(final String collection, final JsonNode documents) {
    final AtomicReference<DataSeederStrategy> dataSeederStrategyAtomicReference =
        new AtomicReference<>();
    final var list = dataSeederStrategyList.dataSeederStrategies();
    for (final var strategy : list) {
      if (strategy.getCollectionCode() != null
          && strategy.getCollectionCode().equalsIgnoreCase(collection)) {
        dataSeederStrategyAtomicReference.set(strategy);
        break;
      }
    }
    if (dataSeederStrategyAtomicReference.get() == null) {
      log.error("Coleção desconhecida: {}", collection);
      return;
    }
    final DataSeederStrategy dataSeederStrategy = dataSeederStrategyAtomicReference.get();
    dataSeederStrategy.seed(documents);
  }
}
