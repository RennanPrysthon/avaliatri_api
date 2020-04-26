package br.avaliatri.excecoes;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ValidationError extends ResponseError{
    private List<FieldMessage> validatorErrors = new ArrayList<>();

    public void addError(String fieldName, String message){
        validatorErrors.add(new FieldMessage(fieldName, message));
    }
}
