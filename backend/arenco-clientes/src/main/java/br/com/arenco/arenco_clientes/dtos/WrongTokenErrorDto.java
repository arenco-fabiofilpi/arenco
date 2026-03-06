package br.com.arenco.arenco_clientes.dtos;


public record WrongTokenErrorDto(String code, String message, boolean otpTokenValid) {}
