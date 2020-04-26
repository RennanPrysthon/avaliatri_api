package br.avaliatri.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SenhaInsertValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SenhaInsert {

    String message() default "Senha invalida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}