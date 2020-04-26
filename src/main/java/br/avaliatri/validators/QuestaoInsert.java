package br.avaliatri.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = QuestaoInsertValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QuestaoInsert {

    String message() default "Questao invalida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}