package br.com.arenco.arenco_clientes.dtos;

import jakarta.validation.constraints.NotEmpty;

public record ChangeCurrentUserPasswordRequest(@NotEmpty String oldPassword, @NotEmpty String newPassword) {}
