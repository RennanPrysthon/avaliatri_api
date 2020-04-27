package br.avaliatri.validators;

import br.avaliatri.dtos.ProvaDTO;
import br.avaliatri.dtos.QuestaoDTO;
import br.avaliatri.excecoes.FieldMessage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ProvaInsertValidator implements ConstraintValidator<ProvaInsert, ProvaDTO> {

    private static final String VALORES_ACEITOS = "ABCDE";

    @Override
    public boolean isValid(ProvaDTO dto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        for(QuestaoDTO questaoDTO: dto.getQuestoes()){
            if(!VALORES_ACEITOS.contains(questaoDTO.getAlternativa_correta())) {
                list.add(new FieldMessage("questoes", "Valor para a alternativa incorreta"));
            }
            if(questaoDTO.getAlternativaA().equals("")) {
                list.add(new FieldMessage("questoes", "Insira o valor para a alternativa A"));
            }
            if(questaoDTO.getAlternativaB().equals("")) {
                list.add(new FieldMessage("questoes", "Insira o valor para a alternativa B"));
            }
            if(questaoDTO.getAlternativaC().equals("")) {
                list.add(new FieldMessage("questoes", "Insira o valor para a alternativa C"));
            }
            if(questaoDTO.getAlternativaD().equals("")) {
                list.add(new FieldMessage("questoes", "Insira o valor para a alternativa D"));
            }
            if(questaoDTO.getAlternativaE().equals("")) {
                list.add(new FieldMessage("questoes", "Insira o valor para a alternativa E"));
            }
        }

        for (FieldMessage e: list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }

    @Override
    public void initialize(ProvaInsert ann) {}

}
