package br.com.arenco.arenco_clientes.exceptions;

public class PasswordRecoveryModelNotFound extends RuntimeException {
  public PasswordRecoveryModelNotFound() {
    super(
        "Nao foi encontrado nenhum Processo de Esqueci Minha Senha para o Usuário/IP/User-Agent informados");
  }
}
