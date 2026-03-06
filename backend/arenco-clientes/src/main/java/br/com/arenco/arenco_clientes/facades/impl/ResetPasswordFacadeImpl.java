package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.facades.ResetPasswordFacade;
import br.com.arenco.arenco_clientes.services.*;
import br.com.arenco.arenco_clientes.exceptions.PasswordNotSetException;
import br.com.arenco.arenco_clientes.exceptions.WrongToken;
import br.com.arenco.arenco_clientes.services.ArencoUserAuthService;
import br.com.arenco.arenco_clientes.entities.PasswordRecoveryModel;
import br.com.arenco.arenco_clientes.entities.UserModel;
import br.com.arenco.arenco_clientes.enums.OtpType;
import br.com.arenco.arenco_clientes.services.OTPMessengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResetPasswordFacadeImpl implements ResetPasswordFacade {

  private final OtpLogService otpLogService;
  private final UserDetailsService userDetailsService;
  private final OTPMessengerService otpMessengerService;
  private final ArencoUserAuthService arencoUserAuthService;
  private final PasswordRecoveryService passwordRecoveryService;
  private final PasswordRecoveryOtpService passwordRecoveryOtpService;
  private final AuthenticationContactService authenticationContactService;

  @Override
  public void askPasswordReset(final String username) {
    log.debug("Solicitação de redefinição de senha: Obtendo o modelo de usuário");
    try {
      userDetailsService.loadUserByUsername(username);
    } catch (final PasswordNotSetException ignored) {
    }
    final var user =
        arencoUserAuthService.findUserByUsername(username, UsernameNotFoundException.class);
    final var contactModel = authenticationContactService.getPreferredContact(user);
    otpLogService.check(user.getId(), contactModel.getValue(), OtpType.PASSWORD_RECOVERY);
    final var passwordRecoveryModel = passwordRecoveryService.create(user);
    final var passwordRecoveryOtpModel =
        passwordRecoveryOtpService.create(user, contactModel, passwordRecoveryModel.getExpiresAt());

    passwordRecoveryService.updateOtpId(passwordRecoveryModel, passwordRecoveryOtpModel.getId());
    otpMessengerService.send(user.getName(), contactModel, passwordRecoveryOtpModel.getToken());
    otpLogService.register(
        user.getId(),
        contactModel.getValue(),
        passwordRecoveryModel.getExpiresAt(),
        OtpType.PASSWORD_RECOVERY);
  }

  @Override
  public void validateResetPasswordToken(final String username, final String token) {
    log.debug(
        "validateResetPasswordToken - Validando o token de recuperação de senha para o usuário {}",
        username);
    final var user =
        arencoUserAuthService.findUserByUsername(username, UsernameNotFoundException.class);
    getPasswordRecoveryModel(token, user);

    log.debug("validateResetPasswordToken - Token de recuperação de senha validado com sucesso");
  }

  @Override
  public void resetPassword(final String username, final String token, final String password) {
    log.debug(
        "resetPassword - Validando o token de recuperação de senha para o usuário {}", username);
    final var user =
        arencoUserAuthService.findUserByUsername(username, UsernameNotFoundException.class);
    final var passwordRecoveryModel = getPasswordRecoveryModel(token, user);
    arencoUserAuthService.setNewPassword(user, password);
    log.debug("resetPassword - Senha Alterada com sucesso");
    remove(passwordRecoveryModel);
  }

  private PasswordRecoveryModel getPasswordRecoveryModel(final String token, final UserModel user) {
    final var passwordRecoveryModel =
        passwordRecoveryService.getPasswordRecoveryModel(user.getId());
    final var otpIdAsString = passwordRecoveryModel.getOtpId();
    if (otpIdAsString == null) {
      throw new IllegalStateException("OTP ID is null for Password Recovery Model");
    }
    try {
      passwordRecoveryOtpService.validate(otpIdAsString, token);
    } catch (final WrongToken e) {
      if (!e.isOtpTokenValid()) {
        remove(passwordRecoveryModel);
      }
      throw e;
    }
    return passwordRecoveryModel;
  }

  private void remove(final PasswordRecoveryModel passwordRecoveryModel) {
    log.warn("Processo de Esqueci Minha Senha sendo invalidado");
    passwordRecoveryService.remove(passwordRecoveryModel);
  }
}
