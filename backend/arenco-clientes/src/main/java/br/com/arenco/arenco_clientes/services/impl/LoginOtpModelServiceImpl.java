package br.com.arenco.arenco_clientes.services.impl;

import br.com.arenco.arenco_clientes.services.LoginOtpModelService;
import br.com.arenco.arenco_clientes.context.RequestContext;
import br.com.arenco.arenco_clientes.exceptions.OtpNotFound;
import br.com.arenco.arenco_clientes.exceptions.WrongToken;
import br.com.arenco.arenco_clientes.utils.ArencoOtpUtils;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.entities.LoginOtpModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.repositories.LoginOtpModelRepository;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginOtpModelServiceImpl implements LoginOtpModelService {
  private static final int MAX_ATTEMPTS = 3;

  private final LoginOtpModelRepository loginOtpModelRepository;

  @Override
  public LoginOtpModel createOneTimePassword(
      final ContactModel contactModel, final UserModel userModel) {
    final var requestContext = RequestContext.get();
    final var userAgent = requestContext.userAgent();
    final var ip = requestContext.ip();

    log.info("🔐 Gerando Login OTP para IP={} | UserAgent={}", ip, userAgent);

    final var otpCode = ArencoOtpUtils.generateOtp();
    final var expiration = Instant.now().plus(Duration.ofMinutes(1));

    final LoginOtpModel model = new LoginOtpModel();
    model.setUserModelId(userModel.getId());
    model.setDeliveredTo(contactModel.getValue());
    model.setToken(otpCode);
    model.setIpAddress(ip);
    model.setUserAgent(userAgent);
    model.setExpiresAt(expiration);
    loginOtpModelRepository.save(model);
    log.info("✅ Login OTP criado e salvo: id={}, expiraEm={}", model.getId(), expiration);
    return model;
  }

  @Override
  public void validateOneTimePassword(final UserModel userModel, final String otp) {
    getOneTimePasswordModel(userModel, otp);
  }

  @Override
  public void authenticateOneTimePassword(final UserModel userModel, final String otp) {
    final var otpModel = getOneTimePasswordModel(userModel, otp);
    loginOtpModelRepository.delete(otpModel);
  }

  private LoginOtpModel getOneTimePasswordModel(
      final UserModel userModel, final String oneTimePassword) {
    final var otpOptional = loginOtpModelRepository.findFirstByUserModelId(userModel.getId());
    if (otpOptional.isEmpty()) {
      // and this is a bad request
      throw new OtpNotFound();
    }
    final var otp = otpOptional.get();
    final var otpToken = otp.getToken();
    if (otpToken == null || otpToken.isEmpty()) {
      throw new IllegalStateException("Login OTP token is null or empty");
    }
    if (!otpToken.equalsIgnoreCase(oneTimePassword)) {
      saveWrongAttempt(otp);
      final var attemptsLeft = MAX_ATTEMPTS - otp.getAttempts();
      throw new WrongToken(attemptsLeft, attemptsLeft > 0);
    }
    return otp;
  }

  private void saveWrongAttempt(final LoginOtpModel otpModel) {
    final var newAttempts = otpModel.getAttempts() + 1;
    otpModel.setAttempts(newAttempts);
    if (newAttempts >= MAX_ATTEMPTS) {
      loginOtpModelRepository.delete(otpModel);
    } else {
      loginOtpModelRepository.save(otpModel);
    }
  }
}
