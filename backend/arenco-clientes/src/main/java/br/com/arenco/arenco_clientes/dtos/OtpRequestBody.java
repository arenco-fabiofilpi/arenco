package br.com.arenco.arenco_clientes.dtos;

import jakarta.validation.constraints.NotEmpty;

public record OtpRequestBody(@NotEmpty(message = "Necessário informar o OTP") String otp) {}
