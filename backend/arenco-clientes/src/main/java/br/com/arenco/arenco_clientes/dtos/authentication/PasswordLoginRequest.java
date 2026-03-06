package br.com.arenco.arenco_clientes.dtos.authentication;

import jakarta.validation.constraints.NotEmpty;

public record PasswordLoginRequest(@NotEmpty String username, @NotEmpty String password) {}
