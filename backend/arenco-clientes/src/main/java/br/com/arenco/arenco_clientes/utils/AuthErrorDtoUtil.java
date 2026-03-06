package br.com.arenco.arenco_clientes.utils;

import br.com.arenco.arenco_clientes.dtos.WrongTokenErrorDto;
import br.com.arenco.arenco_clientes.dtos.misc.CooldownErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public enum AuthErrorDtoUtil {
  ;

  public static ResponseEntity<WrongTokenErrorDto> convertToErrorDto(
      final HttpStatus status, final String message, final boolean otpTokenValid) {
    return ResponseEntity.status(status)
        .body(new WrongTokenErrorDto(status.toString(), message, otpTokenValid));
  }

  public static ResponseEntity<CooldownErrorDto> convertToErrorDto(
      final String message, final long seconds) {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
        .body(new CooldownErrorDto(HttpStatus.TOO_MANY_REQUESTS.toString(), message, seconds));
  }
}
