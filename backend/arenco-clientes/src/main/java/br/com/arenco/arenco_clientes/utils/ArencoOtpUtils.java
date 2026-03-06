package br.com.arenco.arenco_clientes.utils;

import java.security.SecureRandom;

public enum ArencoOtpUtils {
  ;
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  public static String generateOtp() {
    int randomNumber = SECURE_RANDOM.nextInt(900000) + 100000;
    return String.valueOf(randomNumber);
  }

}
