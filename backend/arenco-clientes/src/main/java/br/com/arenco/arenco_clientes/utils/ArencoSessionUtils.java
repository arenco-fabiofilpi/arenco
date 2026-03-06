package br.com.arenco.arenco_clientes.utils;

import br.com.arenco.arenco_clientes.exceptions.ArencoNoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public enum ArencoSessionUtils {
  ;

  public static String hashSessionId(final String uuid, final String pepper) {
    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      final byte[] hash = digest.digest((uuid + pepper).getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    } catch (final NoSuchAlgorithmException e) {
      throw new ArencoNoSuchAlgorithmException(e);
    }
  }
}
