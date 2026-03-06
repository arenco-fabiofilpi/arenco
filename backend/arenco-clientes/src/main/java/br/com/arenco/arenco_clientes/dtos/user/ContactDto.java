package br.com.arenco.arenco_clientes.dtos.user;

import lombok.Builder;

@Builder
public record ContactDto(
    String id,
    String contactMethod,
    String contactValue) {}
