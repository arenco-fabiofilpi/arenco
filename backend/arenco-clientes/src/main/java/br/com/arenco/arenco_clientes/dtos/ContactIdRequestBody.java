package br.com.arenco.arenco_clientes.dtos;

import jakarta.validation.constraints.NotEmpty;

public record ContactIdRequestBody(@NotEmpty(message = "Necessário informar ID do Contato") String contactId) {}
