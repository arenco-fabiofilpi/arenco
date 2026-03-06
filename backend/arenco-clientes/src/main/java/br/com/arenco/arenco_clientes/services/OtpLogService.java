package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.enums.OtpType;
import java.time.Instant;

public interface OtpLogService {
  void register(
      final String userModelId,
      final String deliveredTo,
      final Instant expiration,
      final OtpType type);

  void check(final String userModelId, final String deliveredTo, final OtpType type);
}
