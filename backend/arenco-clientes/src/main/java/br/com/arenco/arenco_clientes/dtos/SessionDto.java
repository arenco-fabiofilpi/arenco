package br.com.arenco.arenco_clientes.dtos;

public record SessionDto(String id, String username, String ip, String userAgent, String createdAt, String expiresAt) {}
