package br.com.arenco.dataseeder.strategies;

import com.fasterxml.jackson.databind.JsonNode;

public interface DataSeederStrategy {
  String getCollectionCode();

  void seed(final JsonNode documents);
}
