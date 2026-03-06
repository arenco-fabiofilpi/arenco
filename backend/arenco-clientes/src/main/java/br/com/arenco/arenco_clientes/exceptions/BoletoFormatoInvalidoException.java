package br.com.arenco.arenco_clientes.exceptions;

public class BoletoFormatoInvalidoException extends ArencoContratosBadRequest {
  public BoletoFormatoInvalidoException(final String message) {
    super(message);
  }
}
