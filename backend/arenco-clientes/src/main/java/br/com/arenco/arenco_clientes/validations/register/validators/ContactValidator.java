package br.com.arenco.arenco_clientes.validations.register.validators;

import br.com.arenco.arenco_clientes.utils.ValidatorUtils;
import br.com.arenco.arenco_clientes.validations.register.annotations.ValidContact;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ContactValidator implements ConstraintValidator<ValidContact, String> {

  private static final String CELULAR_PATTERN = "\\+55\\d{2}\\d{9}";
  private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

  @Override
  public void initialize(final ValidContact constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(final String value, final ConstraintValidatorContext context) {
    if (value == null || value.isEmpty()) {
      ValidatorUtils.buildAndAddConstraintViolationWithTemplate(
          "O campo de contato não pode ser vazio.", context);
      return false;
    }
    final var patternMatch = value.matches(CELULAR_PATTERN) || value.matches(EMAIL_PATTERN);
    if (!patternMatch) {
      ValidatorUtils.buildAndAddConstraintViolationWithTemplate(
          "O campo de contato deve ser um e-mail (enzo.oliveira@bol.com.br) ou um número de celular (+5516996472829).",
          context);
      return false;
    }
    return true;
  }
}
