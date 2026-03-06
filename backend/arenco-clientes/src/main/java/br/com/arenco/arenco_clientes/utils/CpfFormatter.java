package br.com.arenco.arenco_clientes.utils;

public final class CpfFormatter {
  private CpfFormatter() {
    throw new IllegalStateException("Utility class");
  }

  public static String formatCpf(final String input) {
    String numbers = input.replaceAll("\\D", "");

    if (numbers.length() > 11) {
      numbers = numbers.substring(0, 11);
    }

    return switch (numbers.length()) {
      case 0 -> "";
      case 1, 2, 3 -> numbers;
      case 4, 5, 6 -> numbers.substring(0, 3) + "." + numbers.substring(3);
      case 7, 8, 9 ->
          numbers.substring(0, 3) + "." + numbers.substring(3, 6) + "." + numbers.substring(6);
      case 10, 11 ->
          numbers.substring(0, 3)
              + "."
              + numbers.substring(3, 6)
              + "."
              + numbers.substring(6, 9)
              + "-"
              + numbers.substring(9);
      default -> numbers;
    };
  }
}
