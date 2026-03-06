package br.com.arenco.arenco_clientes.exceptions;

import lombok.Getter;

@Getter
public class MultipleOtpRequests extends RuntimeException {
  private final long seconds;

  public MultipleOtpRequests(final long seconds) {
    super(
        String.format("Por favor, aguarde mais %s segundos para solicitar um novo token", seconds));
    this.seconds = seconds;
  }
}
