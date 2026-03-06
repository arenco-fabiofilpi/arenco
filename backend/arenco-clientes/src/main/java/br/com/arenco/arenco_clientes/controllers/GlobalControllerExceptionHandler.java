package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.exceptions.*;
import br.com.arenco.arenco_clientes.dtos.misc.ErrorDto;
import br.com.arenco.arenco_clientes.utils.ErrorDtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

  @ExceptionHandler(OldPasswordInvalidException.class)
  public ResponseEntity<ErrorDto> handleOldPasswordInvalidException(
      final OldPasswordInvalidException e) {
    log.info("handleOldPasswordInvalidException - Message {}", e.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.UNAUTHORIZED, "Senha antiga inválida");
  }

  @ExceptionHandler(IpNotInformedException.class)
  public ResponseEntity<ErrorDto> handleIpNotInformedException(final IpNotInformedException ex) {
    log.error("handleIpNotInformedException: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.FORBIDDEN, ex.getMessage());
  }

  @ExceptionHandler(UserAgentNotInformedException.class)
  public ResponseEntity<ErrorDto> handleUserAgentNotInformedException(
      final UserAgentNotInformedException ex) {
    log.error("handleUserAgentNotInformedException: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.FORBIDDEN, ex.getMessage());
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  private ResponseEntity<ErrorDto> handleUsernameNotFoundException(
      final UsernameNotFoundException ignored) {
    log.info("Username not found");
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.NOT_FOUND, "Username not found");
  }

  @ExceptionHandler(UserIdNotFoundException.class)
  private ResponseEntity<ErrorDto> handleUserIdNotFoundException(
      final UserIdNotFoundException ignored) {
    log.info("User UUID not found");
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.NOT_FOUND, "User UUID not found");
  }

  @ExceptionHandler(InvalidOtpException.class)
  public ResponseEntity<ErrorDto> handleInvalidPasswordRecoveryTokenException(
      final InvalidOtpException ex) {
    log.error("handleInvalidPasswordRecoveryTokenException: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.UNAUTHORIZED, ex.getMessage());
  }

  @ExceptionHandler(OtpExceededTriesException.class)
  public ResponseEntity<ErrorDto> handlePasswordRecoveryTokenExceededTriesException(
      final OtpExceededTriesException ex) {
    log.error("handlePasswordRecoveryTokenExceededTriesException: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.TOO_MANY_REQUESTS, ex.getMessage());
  }

  @ExceptionHandler(WrongPasswordRecoveryTokenException.class)
  public ResponseEntity<ErrorDto> handleWrongPasswordRecoveryTokenException(
      final WrongPasswordRecoveryTokenException ex) {
    log.error("handleWrongPasswordRecoveryTokenException: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorDto> handleBadCredentials(final BadCredentialsException ex) {
    log.error("handleBadCredentials: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.FORBIDDEN, "Credenciais inválidas");
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ErrorDto> handleAuthorizationDeniedException(
      final AuthorizationDeniedException ex) {
    log.error("handleAuthorizationDeniedException: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.FORBIDDEN, ex.getMessage());
  }

  @ExceptionHandler(MultipleSessionsNotHandled.class)
  public ResponseEntity<ErrorDto> handleMultipleSessionsNotHandled(
      final MultipleSessionsNotHandled ex) {
    log.error("handleMultipleSessionsNotHandled: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorDto> handleNoResourceFoundException(
      final NoResourceFoundException ex) {
    log.error("handleNoResourceFoundException: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(AccountStatusException.class)
  public ResponseEntity<ErrorDto> handleAccountStatusException(final AccountStatusException e) {
    log.warn("Conta Bloqueada {}", e.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.UNAUTHORIZED, "Conta Bloqueada");
  }

  @ExceptionHandler(MultiplePasswordRecoveryTokenRequestException.class)
  public ResponseEntity<ErrorDto> handleMultiplePasswordRecoveryTokenRequestException(
      final MultiplePasswordRecoveryTokenRequestException e) {
    log.info("Token still valid. Message {}", e.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.UNAUTHORIZED, e.getMessage());
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorDto> handleIllegalStateException(final AccountStatusException e) {
    log.warn("IllegalStateException. Message {}", e.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.UNAUTHORIZED, e.getMessage());
  }

  @ExceptionHandler(BuildCustomerFiltersBadRequestException.class)
  public ResponseEntity<ErrorDto> handleBuildCustomerFiltersBadRequestException(
      final BuildCustomerFiltersBadRequestException e) {
    log.warn("BuildCustomerFiltersBadRequestException. Message {}", e.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorDto> handleBadRequestException(final BadRequestException ex) {
    log.error("handleBadRequestException: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(InvalidDtoException.class)
  public ResponseEntity<ErrorDto> handleInvalidDtoException(final InvalidDtoException e) {
    log.error("InvalidDtoException. Message {}", e.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
  }
}
