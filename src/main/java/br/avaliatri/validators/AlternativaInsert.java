package br.avaliatri.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AlternativaInsertValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AlternativaInsert {

    String message() default "Alternativa invalida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}