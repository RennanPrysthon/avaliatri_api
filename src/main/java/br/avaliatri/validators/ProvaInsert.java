package br.avaliatri.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ProvaInsertValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProvaInsert {

    String message() default "Prova invalida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}