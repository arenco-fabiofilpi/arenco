package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.context.RequestContext;
import br.com.arenco.arenco_clientes.exceptions.OtpNotFound;
import br.com.arenco.arenco_clientes.exceptions.WrongToken;
import br.com.arenco.arenco_clientes.services.ArencoPreSessionService;
import br.com.arenco.arenco_clientes.services.PreSessionOtpService;
import br.com.arenco.arenco_clientes.utils.ArencoOtpUtils;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.entities.PreSessionOtpModel;
import br.com.arenco.arenco_clientes.repositories.PreSessionOtpdModelRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreSessionOtpServiceImpl implements PreSessionOtpService {
  private static final int MAX_ATTEMPTS = 3;

  private final ArencoPreSessionService arencoPreSessionService;
  private final PreSessionOtpdModelRepository preSessionOtpdModelRepository;

  @Override
  public PreSessionOtpModel createOneTimePassword(final ContactModel contactModel) {
    final var requestContext = RequestContext.get();
    final var userAgent = requestContext.userAgent();
    final var ip = requestContext.ip();

    log.info("🔐 Gerando PreSession OTP para IP={} | UserAgent={}", ip, userAgent);

    final var currentSecurityContext = arencoPreSessionService.getCurrentPreSessionContext();

    final var currentUser =
        arencoPreSessionService.getUserModelFromPreSessionContext(currentSecurityContext);
    final var preSessionModel =
        arencoPreSessionService.getPreSessionModelFromPreSessionContext(currentSecurityContext);

    final var otpCode = ArencoOtpUtils.generateOtp();
    final var expiration = Instant.now().plus(Duration.ofMinutes(1));

    final PreSessionOtpModel model = new PreSessionOtpModel();
    model.setUserModelId(currentUser.getId());
    model.setDeliveredTo(contactModel.getValue());
    model.setToken(otpCode);
    model.setIpAddress(ip);
    model.setUserAgent(userAgent);
    model.setPreSessionId(preSessionModel.getHashedId());
    model.setExpiresAt(expiration);
    preSessionOtpdModelRepository.save(model);
    log.info("✅ PreSession OTP criado e salvo: id={}, expiraEm={}", model.getId(), expiration);
    return model;
  }

  @Override
  public void authenticateOneTimePassword(final String oneTimePassword) {
    final var otpModel = getOneTimePasswordModel(oneTimePassword);
    preSessionOtpdModelRepository.delete(otpModel);
  }

  @Override
  public void validateOneTimePassword(final String oneTimePassword) {
    getOneTimePasswordModel(oneTimePassword);
  }

  @Override
  public Optional<PreSessionOtpModel> findByPreSessionId(final String preSessionId) {
    return preSessionOtpdModelRepository.findByPreSessionId(preSessionId);
  }

  private PreSessionOtpModel getOneTimePasswordModel(final String oneTimePassword) {
    final var currentPreSessionContext = arencoPreSessionService.getCurrentPreSessionContext();
    final var otpReference =
        arencoPreSessionService.getOtpFromPreSessionContext(currentPreSessionContext);
    if (otpReference == null) {
      // this must be treated as internal error since it should not be null
      throw new IllegalStateException("OTP reference is null");
    }
    if (otpReference.get() == null) {
      // and this is a bad request
      throw new OtpNotFound();
    }
    final var otp = otpReference.get();
    final var otpToken = otp.getToken();
    if (otpToken == null || otpToken.isEmpty()) {
      throw new IllegalStateException("OTP token is null or empty");
    }
    if (!otpToken.equalsIgnoreCase(oneTimePassword)) {
      saveWrongAttempt(otp);
      final var attemptsLeft = MAX_ATTEMPTS - otp.getAttempts();
      throw new WrongToken(attemptsLeft, attemptsLeft > 0);
    }
    return otp;
  }

  private void saveWrongAttempt(final PreSessionOtpModel preSessionOtpModel) {
    final var newAttempts = preSessionOtpModel.getAttempts() + 1;
    preSessionOtpModel.setAttempts(newAttempts);
    if (newAttempts >= MAX_ATTEMPTS) {
      preSessionOtpdModelRepository.delete(preSessionOtpModel);
    } else {
      preSessionOtpdModelRepository.save(preSessionOtpModel);
    }
  }
}
