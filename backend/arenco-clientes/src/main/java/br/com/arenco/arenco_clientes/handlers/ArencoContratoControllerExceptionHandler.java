package br.com.arenco.arenco_clientes.handlers;

import br.com.arenco.arenco_clientes.exceptions.ArencoContratosBadRequest;
import br.com.arenco.arenco_clientes.exceptions.ArencoContratosConflict;
import br.com.arenco.arenco_clientes.exceptions.ArencoContratosInternalError;
import br.com.arenco.arenco_clientes.exceptions.ArencoContratosNotFoundException;
import br.com.arenco.arenco_clientes.dtos.misc.ErrorDto;

import br.com.arenco.arenco_clientes.utils.ErrorDtoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ArencoContratoControllerExceptionHandler {
  @ExceptionHandler(ArencoContratosConflict.class)
  public ResponseEntity<ErrorDto> handleArencoContratosConflict(final ArencoContratosConflict ex) {
    log.error("handleArencoContratosConflict: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(ArencoContratosBadRequest.class)
  public ResponseEntity<ErrorDto> handleArencoContratosBadRequest(
      final ArencoContratosBadRequest ex) {
    log.error("handleArencoContratosBadRequest: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(ArencoContratosNotFoundException.class)
  public ResponseEntity<ErrorDto> handleArencoContratosNotFoundException(
      final ArencoContratosNotFoundException ex) {
    log.error("handleArencoContratosNotFoundException: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(ArencoContratosInternalError.class)
  public ResponseEntity<ErrorDto> handleArencoContratosInternalError(
      final ArencoContratosInternalError ex) {
    log.error("handleArencoContratosInternalError: {}", ex.getMessage(), ex);
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }
}
