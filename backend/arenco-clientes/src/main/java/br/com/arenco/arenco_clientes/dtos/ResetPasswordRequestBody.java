package br.com.arenco.arenco_clientes.dtos;

import jakarta.validation.constraints.NotEmpty;

public record ResetPasswordRequestBody(
    @NotEmpty(message = "Username não deve estar vazio") String username,
    @NotEmpty(message = "Token não deve estar vazio") String token,
    @NotEmpty(message = "Senha não deve estar vazia") String password) {}
