package br.com.arenco.arenco_clientes.services;

import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.enums.SuspiciousActivityType;

public interface BruteForceDefenseService {
  void saveSuspiciousActivity(final SuspiciousActivityType suspiciousActivityType);

  boolean isLocked(final UserModel user);
}
