package br.com.arenco.arenco_clientes.handlers;

import br.com.arenco.arenco_clientes.dtos.misc.ErrorDto;
import br.com.arenco.arenco_clientes.utils.ErrorDtoUtil;
import br.com.arenco.arenco_clientes.exceptions.MessengerStrategyNotFound;
import br.com.arenco.arenco_clientes.exceptions.SendMessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ArencoMessengerExceptionHandler {

  @ExceptionHandler(SendMessageException.class)
  public ResponseEntity<ErrorDto> handleSendMessageException(final SendMessageException e) {
    log.error(e.getMessage(), e);
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
  }

  @ExceptionHandler(MessengerStrategyNotFound.class)
  public ResponseEntity<ErrorDto> handleMessengerStrategyNotFound(
      final MessengerStrategyNotFound e) {
    log.error(e.getMessage(), e);
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
  }
}
