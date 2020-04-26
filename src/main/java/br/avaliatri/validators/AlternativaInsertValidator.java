package br.avaliatri.validators;

import br.avaliatri.dtos.AlternativaDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class AlternativaInsertValidator implements ConstraintValidator<AlternativaInsert, AlternativaDTO> {

    private static final String VALORES_ACEITOS = "ABCDE";

    @Override
    public boolean isValid(AlternativaDTO dto, ConstraintValidatorContext context) {
        List<String> list = new ArrayList<>();

        if(VALORES_ACEITOS.contains(dto.getAlternativa())){
            list.add(dto.getAlternativa() + " nao e um valor aceito");
        }

        if(dto.getTexto().equals("") || dto.getTexto() == null) {
            list.add("A alternativa " + dto.getAlternativa() + " nao pode ter o valor vazio");
        }

        for (String e: list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e).addPropertyNode(e)
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }

    @Override
    public void initialize(AlternativaInsert ann) {}

}
