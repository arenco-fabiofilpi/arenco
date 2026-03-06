package br.com.arenco.arenco_clientes.utils;

import jakarta.validation.ConstraintValidatorContext;

public final class ValidatorUtils {
  private ValidatorUtils() {
    throw new AssertionError();
  }

  public static void buildAndAddConstraintViolationWithTemplate(
      final String message, final ConstraintValidatorContext context) {
    if (context != null) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
  }
}
