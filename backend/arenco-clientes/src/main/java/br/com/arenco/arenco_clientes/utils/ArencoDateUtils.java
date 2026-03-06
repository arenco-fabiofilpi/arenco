package br.com.arenco.arenco_clientes.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public enum ArencoDateUtils {
  ;
  private static final ZoneId Z_SP = ZoneId.of("America/Sao_Paulo");

  private static final DateTimeFormatter BR_DATE_TIME =
      DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss").withZone(Z_SP);

  private static final DateTimeFormatter BR_DATE =
      DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(Z_SP);

  public static int getCookieMaxAge(final Instant expiration) {
    if (expiration == null) {
      throw new IllegalArgumentException("Instant can't be null");
    }
    // Calcula o tempo de expiração em segundos a partir de agora
    final long maxAgeSeconds = Duration.between(Instant.now(), expiration).getSeconds();
    // Garante que o maxAge seja no mínimo 0
    return (int) Math.max(0, maxAgeSeconds);
  }

  public static long calcularDiferencaEmSegundos(
      final Instant instanteInicial, final Instant instanteFinal, final long margemDeSeguranca) {
    if (instanteInicial == null || instanteFinal == null) {
      throw new IllegalArgumentException("Os objetos Instant não podem ser nulos");
    }

    return instanteInicial.until(instanteFinal, ChronoUnit.SECONDS) + margemDeSeguranca;
  }

  public static String fromLocalDateToString(final LocalDate localDate) {
    if (localDate == null) {
      return null;
    }
    return BR_DATE.format(localDate);
  }

  public static String fromInstantToString(final Instant instante) {
    if (instante == null) {
      return null;
    }
    return BR_DATE_TIME.format(instante);
  }
}
