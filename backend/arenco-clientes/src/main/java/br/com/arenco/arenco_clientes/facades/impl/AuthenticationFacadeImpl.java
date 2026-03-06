package br.com.arenco.arenco_clientes.facades.impl;

import br.com.arenco.arenco_clientes.facades.AuthenticationFacade;
import br.com.arenco.arenco_clientes.services.OtpLogService;
import br.com.arenco.arenco_clientes.constants.ArencoAuthLibConstants;
import br.com.arenco.arenco_clientes.exceptions.ContactNotFoundException;
import br.com.arenco.arenco_clientes.services.*;
import br.com.arenco.arenco_clientes.entities.ContactModel;
import br.com.arenco.arenco_clientes.enums.LoginMethod;
import br.com.arenco.arenco_clientes.enums.OtpType;
import br.com.arenco.arenco_clientes.services.OTPMessengerService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFacadeImpl implements AuthenticationFacade {
  private final CookieService cookieService;
  private final OtpLogService otpLogService;
  private final OTPMessengerService otpMessengerService;
  private final ArencoSessionService arencoSessionService;
  private final PreSessionOtpService preSessionOtpService;
  private final ArencoUserAuthService arencoUserAuthService;
  private final ArencoPreSessionService arencoPreSessionService;
  private final ArencoAuthLibContactService arencoAuthLibContactService;

  @Override
  public HttpStatus login(
      final HttpServletResponse response, final String username, final String password) {
    final var user =
        arencoUserAuthService.findUserByUsername(username, BadCredentialsException.class);
    arencoUserAuthService.authenticateUserAndPassword(user, password);

    if (user.getLoginMethod() == LoginMethod.TWO_FACTOR_AUTHENTICATION) {
      cookieService.createPreSessionCookie(response, user);
      return HttpStatus.PARTIAL_CONTENT;
    }
    cookieService.createSessionCookie(response, user);
    return HttpStatus.OK;
  }

  @Override
  public void logout(final HttpServletRequest request, final HttpServletResponse response) {
    removePreSession(request);
    cookieService.removePreSessionCookie(response);
    removeSession(request);
    cookieService.removeSessionCookie(response);
  }

  @Override
  public void send2FactorAuthOtp(final String contactId) {
    final var userModel = arencoPreSessionService.getUserModelFromPreSessionContext();
    final var contacts = arencoAuthLibContactService.findByUser(userModel);
    final AtomicReference<ContactModel> contactModelAtomicReference = new AtomicReference<>();
    for (final var contact : contacts) {
      if (StringUtils.equalsIgnoreCase(contactId, contact.getId())) {
        contactModelAtomicReference.set(contact);
        break;
      }
    }
    if (contactModelAtomicReference.get() == null) {
      throw new ContactNotFoundException(String.format("Contact %s not found", contactId));
    }
    final var contactModel = contactModelAtomicReference.get();
    otpLogService.check(
        userModel.getId(), contactModel.getValue(), OtpType.SECOND_FACTOR_AUTHENTICATION);
    final var oneTimePasswordModel = preSessionOtpService.createOneTimePassword(contactModel);
    otpLogService.register(
        userModel.getId(),
        contactModel.getValue(),
        oneTimePasswordModel.getExpiresAt(),
        OtpType.SECOND_FACTOR_AUTHENTICATION);
    otpMessengerService.send(userModel.getName(), contactModel, oneTimePasswordModel.getToken());
  }

  @Override
  public void validateSecondFactorAuthentication(final String oneTimePassword) {
    preSessionOtpService.validateOneTimePassword(oneTimePassword);
  }

  @Override
  public void authenticateWithSecondFactorAuthentication(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final String oneTimePassword) {
    preSessionOtpService.authenticateOneTimePassword(oneTimePassword);
    final var userModel = arencoPreSessionService.getUserModelFromPreSessionContext();
    cookieService.createSessionCookie(response, userModel);
    removePreSession(request);
    cookieService.removePreSessionCookie(response);
  }

  private void removePreSession(final HttpServletRequest request) {
    final Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (final Cookie cookie : cookies) {
        if (ArencoAuthLibConstants.PRE_SESSION_ID_COOKIE_NAME.equals(cookie.getName())) {
          final String id = cookie.getValue();
          arencoPreSessionService.removePreSession(id);
        }
      }
    }
  }

  private void removeSession(final HttpServletRequest request) {
    final Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (final Cookie cookie : cookies) {
        if (ArencoAuthLibConstants.SESSION_ID_COOKIE_NAME.equals(cookie.getName())) {
          final String sessionId = cookie.getValue();
          arencoSessionService.removeSession(sessionId);
        }
      }
    }
  }
}
