package br.com.arenco.arenco_clientes.exceptions;

public class TituloNaoEncontrado extends ArencoContratosNotFoundException {
  public TituloNaoEncontrado(final String message) {
    super(message);
  }
}
