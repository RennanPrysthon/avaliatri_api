package br.avaliatri.validators;

import br.avaliatri.dtos.UsuarioDTO;
import br.avaliatri.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;

public class UsuarioInsertValidator implements ConstraintValidator<UsuarioInsert, UsuarioDTO> {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public boolean isValid(UsuarioDTO dto, ConstraintValidatorContext context) {
        HashMap<String, String> list = new HashMap<>();

        repository.findByEmail(dto.getEmail())
            .ifPresent(u -> {
                if(u.getIs_active()) {
                    list.put("email", "O email " + u.getEmail() + " ja esta configurado a uma conta cadastrada");
                } else {
                    list.put("email", "O email " + u.getEmail() + " ja esta configurado a uma conta inativa. Solicite o ativamento.");
                }
            });

        list.forEach((k, v) -> {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(v).addPropertyNode(k)
                    .addConstraintViolation();

        });

        return list.isEmpty();
    }

    @Override
    public void initialize(UsuarioInsert ann) {}

}
