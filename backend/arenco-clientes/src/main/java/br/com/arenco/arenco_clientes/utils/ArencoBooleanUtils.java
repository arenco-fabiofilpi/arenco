package br.com.arenco.arenco_clientes.utils;

public final class ArencoBooleanUtils {
  private ArencoBooleanUtils() {
    throw new IllegalStateException("Utility class");
  }

  public static boolean isBooleanValue(final String value) {
    return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
  }
}
