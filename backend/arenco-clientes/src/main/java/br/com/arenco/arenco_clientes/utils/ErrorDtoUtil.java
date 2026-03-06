package br.com.arenco.arenco_clientes.utils;

import br.com.arenco.arenco_clientes.dtos.misc.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public enum ErrorDtoUtil {
  ;

  public static ResponseEntity<ErrorDto> convertToErrorDto(
      final HttpStatus status, final String message) {
    return ResponseEntity.status(status).body(new ErrorDto(status.toString(), message));
  }
}
