package br.com.arenco.arenco_clientes.validations.password.validators;

import br.com.arenco.arenco_clientes.validations.password.annotations.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

  @Override
  public void initialize(ValidPassword constraintAnnotation) {
    log.info("Inicializando validador de senha");
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.length() < 8) {
      return false;
    }

    boolean hasUpperCase = value.chars().anyMatch(Character::isUpperCase);
    boolean hasLowerCase = value.chars().anyMatch(Character::isLowerCase);
    boolean hasDigit = value.chars().anyMatch(Character::isDigit);
    boolean hasSpecialChar = value.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));

    return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
  }
}
