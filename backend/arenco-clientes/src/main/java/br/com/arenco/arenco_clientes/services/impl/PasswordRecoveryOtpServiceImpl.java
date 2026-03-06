package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.PasswordRecoveryOtpService;
import br.com.arenco.arenco_clientes.context.RequestContext;
import br.com.arenco.arenco_clientes.exceptions.WrongToken;
import br.com.arenco.arenco_clientes.utils.ArencoOtpUtils;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.entities.PasswordRecoveryOtpModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.repositories.PasswordRecoveryOtpModelRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordRecoveryOtpServiceImpl implements PasswordRecoveryOtpService {
  private static final int MAX_ATTEMPTS = 5;

  private final PasswordRecoveryOtpModelRepository repository;

  @Override
  public PasswordRecoveryOtpModel create(
      final UserModel userModel, final ContactModel contactModel, final Instant expiresAt) {
    final var requestContext = RequestContext.get();
    final var userAgent = requestContext.userAgent();
    final var ip = requestContext.ip();
    log.info("🔐 Gerando Password Recovery OTP para IP={} | UserAgent={}", ip, userAgent);

    final var otpCode = ArencoOtpUtils.generateOtp();

    final PasswordRecoveryOtpModel model =
        new PasswordRecoveryOtpModel();
    model.setUserModelId(userModel.getId());
    model.setDeliveredTo(contactModel.getValue());
    model.setToken(otpCode);
    model.setIpAddress(ip);
    model.setUserAgent(userAgent);
    model.setExpiresAt(expiresAt);
    repository.save(model);
    log.info(
        "✅ Password Recovery OTP criado e salvo: id={}, expiraEm={}", model.getId(), expiresAt);
    return model;
  }

  @Override
  public void validate(final String otpId, final String token) {
    final var otpModel = getPasswordRecoveryOtpModel(otpId);
    final var expectedValue = otpModel.getToken();
    if (!StringUtils.equalsIgnoreCase(expectedValue, token)) {
      saveBadAttempt(otpModel);
      final var attemptsLeft = MAX_ATTEMPTS - otpModel.getAttempts();
      throw new WrongToken(attemptsLeft, attemptsLeft > 0);
    }
    log.info("Token is valid");
  }

  private PasswordRecoveryOtpModel getPasswordRecoveryOtpModel(final String otpId) {
    return repository
        .findById(otpId)
        .orElseThrow(
            () -> new IllegalStateException("O Password Recovery OTP Model não foi encontrado"));
  }

  private void saveBadAttempt(final PasswordRecoveryOtpModel otpModel) {
    final var newAttempts = otpModel.getAttempts() + 1;
    otpModel.setAttempts(newAttempts);
    if (newAttempts >= MAX_ATTEMPTS) {
      repository.delete(otpModel);
    } else {
      repository.save(otpModel);
    }
  }
}
