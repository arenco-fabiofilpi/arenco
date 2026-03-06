package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.exceptions.PasswordRecoveryModelNotFound;
import br.com.arenco.arenco_clientes.services.PasswordRecoveryService;
import br.com.arenco.arenco_clientes.context.RequestContext;
import br.com.arenco.arenco_clientes.entities.PasswordRecoveryModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.repositories.PasswordRecoveryModelRepository;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {

  private final PasswordRecoveryModelRepository repository;

  @Override
  public PasswordRecoveryModel create(final UserModel user) {
    final var context = RequestContext.get();
    final var ip = context.ip();
    final var userAgent = context.userAgent();
    final var expiration = Instant.now().plus(Duration.ofMinutes(1));
    final var model = new PasswordRecoveryModel();
    model.setIp(ip);
    model.setUserAgent(userAgent);
    model.setUserId(user.getId());
    model.setExpiresAt(expiration);
    repository.save(model);
    log.info("Created PasswordRecoveryModel with id={}", model.getId());
    return model;
  }

  @Override
  public void updateOtpId(final PasswordRecoveryModel passwordRecoveryModel, final String otpId) {
    passwordRecoveryModel.setOtpId(otpId);
    repository.save(passwordRecoveryModel);
  }

  @Override
  public PasswordRecoveryModel getPasswordRecoveryModel(final String userId) {
    final var context = RequestContext.get();
    final var ip = context.ip();
    final var userAgent = context.userAgent();
    final var optional =
        repository.findFirstByUserAgentAndIpAndUserIdOrderByDateCreatedDesc(userAgent, ip, userId);
    if (optional.isPresent()) {
      log.debug(
          "Found Password Recovery Model for User ID {} UserAgent {} and IP {}",
          userId,
          userAgent,
          ip);
      return optional.get();
    }
    log.warn("Did NOT Found OTP for User ID {} UserAgent {} and IP {}", userId, userAgent, ip);
    throw new PasswordRecoveryModelNotFound();
  }

  @Override
  public void remove(final PasswordRecoveryModel passwordRecoveryModel) {
    log.info("Removing Password Recovery Model with id={}", passwordRecoveryModel.getId());
    repository.delete(passwordRecoveryModel);
  }
}
