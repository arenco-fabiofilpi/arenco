package br.com.arenco.arenco_clientes.dtos;


public record SmsDto(String from, String deliveredTo, String message, String status, String dateCreated) {}
