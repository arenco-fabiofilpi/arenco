package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.BruteForceDefenseService;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.enums.SuspiciousActivityType;
import br.com.arenco.arenco_clientes.enums.UserState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BruteForceDefenseServiceImpl implements BruteForceDefenseService {
  @Override
  public void saveSuspiciousActivity(final SuspiciousActivityType suspiciousActivityType) {}

  //  private final UserServiceImpl userServiceImpl;
  //
  //  @Override
  //  public void blockUser(final User user) {
  //    log.info("Blocking user {}", user);
  //    user.setState(UserState.BLOCKED);
  //    userServiceImpl.save(user);
  //  }
  //
  //  @Override
  //  public void activateUser(final User user) {
  //    log.info("Activating user {}", user);
  //    user.setState(UserState.ACTIVE);
  //    userServiceImpl.save(user);
  //  }
  //
  @Override
  public boolean isLocked(final UserModel user) {
    log.debug("BruteForceDefenseServiceImpl: Is locked");
    return user.getState() == UserState.BLOCKED;
  }
}
