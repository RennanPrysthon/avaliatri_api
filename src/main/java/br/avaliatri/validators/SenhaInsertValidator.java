package br.avaliatri.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SenhaInsertValidator implements ConstraintValidator<SenhaInsert, String> {

    private static final String CARACTERES_ESPECIAIS = "/[@!#$%]/g";
    private static final String NUMEROS = "/[\\d]/g" ;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        List<String> list = new ArrayList<>();

        if(password.length() < 8) {
            list.add("Senha nao pode ter menos que 8 caracteres");
        }

        if(!Pattern.matches(CARACTERES_ESPECIAIS, password)) {
            list.add("A senha tem que ter no minimo um desses caracteres: " + CARACTERES_ESPECIAIS);
        }

        if(!Pattern.matches(NUMEROS, password)) {
            list.add("A senha tem que conter no minimo um digito");
        }

        for (String e: list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e).addConstraintViolation();
        }
        return list.isEmpty();
    }

    @Override
    public void initialize(SenhaInsert ann) {}

}
