package br.com.arenco.arenco_clientes.constants;

public final class ArencoClientesConstants {
  public static final String TOKEN_ENDPOINT = "/token";
  public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
  public static final String REFRESH_TOKEN_HEADER_NAME = "Refresh-Token";
  public static final String BEARER_TOKEN_HEADER_VALUE_PREFIX = "Bearer ";

  public static final String CUSTOMERS_USER_GROUP_CODE = "clientes";
  public static final String NATURAL_PERSON_CUSTOMERS_USER_GROUP_CODE = "clientesPessoaFisica";
  public static final String LEGAL_ENTITY_CUSTOMERS_USER_GROUP_CODE = "clientesPessoaJuridica";
  public static final String LOGOUT_SUCCESSFUL_ENDPOINT = "/logout/success";

  private ArencoClientesConstants() {
    throw new AssertionError();
  }
}
