package br.com.arenco.arenco_clientes.dtos;

import jakarta.validation.constraints.NotEmpty;

public record UsernameRequestBody(
    @NotEmpty(message = "Username não deve estar vazio") String username) {}
