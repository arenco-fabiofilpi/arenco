package br.com.arenco.arenco_clientes.utils;

import java.time.Duration;

public class DurationParser {
  private DurationParser() {}

  public static Duration parse(String text) {
    if (text.endsWith("ms")) return Duration.ofMillis(Long.parseLong(text.replace("ms", "")));
    if (text.endsWith("s")) return Duration.ofSeconds(Long.parseLong(text.replace("s", "")));
    if (text.endsWith("m")) return Duration.ofMinutes(Long.parseLong(text.replace("m", "")));
    if (text.endsWith("h")) return Duration.ofHours(Long.parseLong(text.replace("h", "")));
    throw new IllegalArgumentException("Formato inválido para refillPeriod: " + text);
  }
}
