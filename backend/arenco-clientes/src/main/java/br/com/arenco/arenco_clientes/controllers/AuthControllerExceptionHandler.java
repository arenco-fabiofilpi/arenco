package br.com.arenco.arenco_clientes.controllers;

import br.com.arenco.arenco_clientes.dtos.WrongTokenErrorDto;
import br.com.arenco.arenco_clientes.exceptions.*;
import br.com.arenco.arenco_clientes.utils.AuthErrorDtoUtil;
import br.com.arenco.arenco_clientes.dtos.misc.CooldownErrorDto;
import br.com.arenco.arenco_clientes.dtos.misc.ErrorDto;
import br.com.arenco.arenco_clientes.utils.ErrorDtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthControllerExceptionHandler {

  @ExceptionHandler(NoContactPreferenceException.class)
  public ResponseEntity<ErrorDto> handleNoContactPreferenceException(
      final NoContactPreferenceException e) {
    log.warn("Nenhum método de contato preferido armazenado {}", e.getMessage());
    return ErrorDtoUtil.convertToErrorDto(
        HttpStatus.INTERNAL_SERVER_ERROR, "Nenhum Metodo de Contato preferido armazenado");
  }

  @ExceptionHandler(MultipleOtpRequests.class)
  public ResponseEntity<CooldownErrorDto> handleMultipleOtpRequests(final MultipleOtpRequests e) {
    log.warn("Multiplas requisicoes de envio de OTP. Mensagem: {}", e.getMessage());
    return AuthErrorDtoUtil.convertToErrorDto(e.getMessage(), e.getSeconds());
  }

  @ExceptionHandler(WrongToken.class)
  public ResponseEntity<WrongTokenErrorDto> handleWrongTokenException(final WrongToken e) {
    log.info("AuthenticationController: WrongToken");
    return AuthErrorDtoUtil.convertToErrorDto(
        HttpStatus.BAD_REQUEST, e.getMessage(), e.isOtpTokenValid());
  }

  @ExceptionHandler(OtpNotFound.class)
  public ResponseEntity<ErrorDto> handleOtpNotFound(final OtpNotFound e) {
    log.info("AuthenticationController: OtpNotFound");
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.UNAUTHORIZED, e.getMessage());
  }

  @ExceptionHandler(PasswordRecoveryModelNotFound.class)
  public ResponseEntity<ErrorDto> handlePasswordRecoveryModelNotFound(
      final PasswordRecoveryModelNotFound e) {
    log.info("AuthenticationController: PasswordRecoveryModelNotFound");
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.UNAUTHORIZED, e.getMessage());
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorDto> handleBadCredentials(final BadCredentialsException ex) {
    log.error("handleBadCredentials: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.FORBIDDEN, "Credenciais inválidas");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(final Exception e) throws Exception {
    log.error(e.getMessage(), e);
    throw e;
  }
}
