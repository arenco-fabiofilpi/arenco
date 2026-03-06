package br.com.arenco.arenco_clientes.validations.register.annotations;

import br.com.arenco.arenco_clientes.validations.register.validators.ContactValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ContactValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidContact {
  String message() default
      "Contato inválido. Se for celular, o formato esperado é +5516999999999. Se for e-mail, o formato esperado é fulano@provedor.com.br";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
