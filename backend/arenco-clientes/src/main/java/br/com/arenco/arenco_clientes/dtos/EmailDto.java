package br.com.arenco.arenco_clientes.dtos;


public record EmailDto(
    String date,
    String from,
    String subject,
    String messageId,
    String templateId,
    String deliveredTo,
    Integer statusCode) {}
