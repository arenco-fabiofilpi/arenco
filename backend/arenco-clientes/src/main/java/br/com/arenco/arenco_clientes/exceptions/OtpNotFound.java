package br.com.arenco.arenco_clientes.exceptions;

public class OtpNotFound extends RuntimeException {
  public OtpNotFound() {
    super("OTP não encontrado");
  }
}
