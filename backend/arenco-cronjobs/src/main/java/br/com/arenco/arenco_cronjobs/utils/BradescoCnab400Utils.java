package br.com.arenco.arenco_cronjobs.utils;

public enum BradescoCnab400Utils {
  ;

  /**
   * Calcula o DV do Nosso Número (Bradesco CNAB400). Regra: concatenar Carteira(3) +
   * NossoNúmero(11), aplicar Módulo 11 com pesos 2..7 (cíclicos). resto==0 -> "0"; resto==1 -> "P";
   * senão -> 11 - resto.
   *
   * @param carteira3 exatamente 3 dígitos (ex.: "021", "009", "026")
   * @param nossoNumero11 exatamente 11 dígitos
   */
  public static String dvNossoNumero(String carteira3, String nossoNumero11) {
    if (carteira3 == null || !carteira3.matches("\\d{3}"))
      throw new IllegalArgumentException("Carteira deve ter exatamente 3 dígitos.");
    if (nossoNumero11 == null || !nossoNumero11.matches("\\d{11}"))
      throw new IllegalArgumentException("Nosso Número deve ter exatamente 11 dígitos.");

    String base = carteira3 + nossoNumero11;
    int[] pesos = {2, 3, 4, 5, 6, 7};
    int soma = 0, idx = 0;

    for (int i = base.length() - 1; i >= 0; i--) {
      int d = base.charAt(i) - '0';
      soma += d * pesos[idx];
      idx = (idx + 1) % pesos.length;
    }

    int resto = soma % 11;
    if (resto == 0) return "0";
    if (resto == 1) return "P";
    return String.valueOf(11 - resto);
  }
}
