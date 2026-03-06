package br.com.arenco.arenco_clientes.dtos.misc;

public record CooldownErrorDto(String code, String message, long seconds) {}
