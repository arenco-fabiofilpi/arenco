package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.exceptions.NoContactPreferenceException;
import br.com.arenco.arenco_clientes.facades.CustomerAuthenticationFacade;
import br.com.arenco.arenco_clientes.services.AuthenticationContactService;
import br.com.arenco.arenco_clientes.services.LoginOtpModelService;
import br.com.arenco.arenco_clientes.services.OtpLogService;
import br.com.arenco.arenco_clientes.services.ArencoUserAuthService;
import br.com.arenco.arenco_clientes.services.CookieService;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.enums.LoginMethod;
import br.com.arenco.arenco_clientes.enums.OtpType;
import br.com.arenco.arenco_clientes.services.OTPMessengerService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerAuthenticationFacadeImpl implements CustomerAuthenticationFacade {
  private final OtpLogService otpLogService;
  private final CookieService cookieService;
  private final OTPMessengerService otpMessengerService;
  private final LoginOtpModelService loginOtpModelService;
  private final ArencoUserAuthService arencoUserAuthService;
  private final AuthenticationContactService authenticationContactService;

  @Override
  public void customerLogin(final String username) {
    final var userModelOptional = arencoUserAuthService.findUserByUsername(username);
    if (userModelOptional.isEmpty()) {
      log.error("customerLogin: Username not found: {}", username);
      return;
    }
    final var userModel = userModelOptional.get();
    if (userModel.getLoginMethod() != LoginMethod.USERNAME_ONLY) {
      log.error(
          "customerLogin: UserModel {} cannot use Customer Login (without password)",
          userModel.getId());
      return;
    }
    final var contactModelOptional = getPreferredContactModel(userModel);
    if (contactModelOptional.isEmpty()) {
      return;
    }
    final var contactModel = contactModelOptional.get();
    otpLogService.check(userModel.getId(), contactModel.getValue(), OtpType.LOGIN_WITHOUT_PASSWORD);
    final var oneTimePasswordModel =
        loginOtpModelService.createOneTimePassword(contactModel, userModel);
    otpLogService.register(
        userModel.getId(),
        contactModel.getValue(),
        oneTimePasswordModel.getExpiresAt(),
        OtpType.LOGIN_WITHOUT_PASSWORD);
    otpMessengerService.send(userModel.getName(), contactModel, oneTimePasswordModel.getToken());
  }

  @Override
  public void validateOtp(final String username, final String otp) {
    final var userModelOptional = arencoUserAuthService.findUserByUsername(username);
    if (userModelOptional.isEmpty()) {
      log.error("validateOtp: Username not found: {}", username);
      return;
    }
    final var userModel = userModelOptional.get();

    loginOtpModelService.validateOneTimePassword(userModel, otp);
  }

  @Override
  public void authenticateOtp(
      final String username, final String otp, final HttpServletResponse response) {
    final var userModelOptional = arencoUserAuthService.findUserByUsername(username);
    if (userModelOptional.isEmpty()) {
      log.error("authenticateOtp: Username not found: {}", username);
      return;
    }
    final var userModel = userModelOptional.get();

    loginOtpModelService.authenticateOneTimePassword(userModel, otp);
    cookieService.createSessionCookie(response, userModel);
  }

  private Optional<ContactModel> getPreferredContactModel(final UserModel userModel) {
    try {
      return Optional.of(authenticationContactService.getPreferredContact(userModel));
    } catch (final NoContactPreferenceException | IllegalArgumentException e) {
      log.error(e.getMessage(), e);
      return Optional.empty();
    }
  }
}
