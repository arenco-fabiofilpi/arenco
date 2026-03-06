package br.com.arenco.arenco_clientes.utils;

public final class CnpjFormatter {
  private CnpjFormatter() {
    throw new IllegalStateException("Utility class");
  }

  public static String formatCnpj(final String input) {
    String numbers = input.replaceAll("\\D", "");

    if (numbers.length() > 14) {
      numbers = numbers.substring(0, 14);
    }

    return switch (numbers.length()) {
      case 0 -> "";
      case 1, 2, 3 -> numbers;
      case 4, 5, 6 -> numbers.substring(0, 2) + "." + numbers.substring(2);
      case 7, 8 ->
          numbers.substring(0, 2) + "." + numbers.substring(2, 5) + "." + numbers.substring(5);
      case 9, 10, 11 ->
          numbers.substring(0, 2)
              + "."
              + numbers.substring(2, 5)
              + "."
              + numbers.substring(5, 8)
              + "/"
              + numbers.substring(8);
      case 12, 13, 14 ->
          numbers.substring(0, 2)
              + "."
              + numbers.substring(2, 5)
              + "."
              + numbers.substring(5, 8)
              + "/"
              + numbers.substring(8, 12)
              + "-"
              + numbers.substring(12);
      default -> numbers;
    };
  }
}
