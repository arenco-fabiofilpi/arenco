package br.com.arenco.arenco_clientes.handlers;

import static br.com.arenco.arenco_clientes.utils.ErrorDtoUtil.convertToErrorDto;

import br.com.arenco.arenco_clientes.dtos.misc.ErrorDto;
import br.com.arenco.arenco_clientes.utils.ErrorDtoUtil;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ArencoDtosExceptionHandlers {
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorDto> handleArencoContratosConflict(
      final MethodArgumentTypeMismatchException ex) {
    log.error("handleArencoContratosConflict: {}", ex.getMessage());
    return convertToErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<?> handleBind(final BindException ex) {
    final var errors =
        ex.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.groupingBy(
                    FieldError::getField,
                    Collectors.mapping(
                        DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("message", "Erro de validação", "errors", errors));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException ex) {
    log.error("handleMethodArgumentNotValidException: {}", ex.getMessage());
    final var returnMessage = new StringBuilder();
    final var errors = ex.getAllErrors();
    for (final var error : errors) {
      if (error.getDefaultMessage() != null) {
        if (!returnMessage.isEmpty()) {
          returnMessage.append(" ");
        }
        returnMessage.append(error.getDefaultMessage()).append(";");
      }
    }
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.BAD_REQUEST, returnMessage.toString());
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorDto> handleNoSuchElementException(final NoSuchElementException ex) {
    log.error("handleNoSuchElementException: {}", ex.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleGeneralExceptions(final Exception ex) {
    log.error(
        "handleGeneralExceptions: {}",
        ex.getMessage() != null ? ex.getMessage() : Arrays.toString(ex.getStackTrace()),
        ex);
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorDto> handleIllegalArgumentException(final IllegalArgumentException e) {
    log.error("IllegalArgumentException. Message {}", e.getMessage());
    return ErrorDtoUtil.convertToErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
  }

}
