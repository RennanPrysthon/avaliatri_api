package br.avaliatri.validators;

import br.avaliatri.dtos.QuestaoDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class QuestaoInsertValidator implements ConstraintValidator<QuestaoInsert, QuestaoDTO> {

    private static final String VALORES_ACEITOS = "ABCDE";

    @Override
    public boolean isValid(QuestaoDTO dto, ConstraintValidatorContext context) {
        List<String> list = new ArrayList<>();

        if(!VALORES_ACEITOS.contains(dto.getAlternativa_correta())) {
            list.add(dto.getAlternativa_correta() + " nao e um valor correto");
        }

        for (String e: list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e).addPropertyNode(e)
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }

    @Override
    public void initialize(QuestaoInsert ann) {}

}
