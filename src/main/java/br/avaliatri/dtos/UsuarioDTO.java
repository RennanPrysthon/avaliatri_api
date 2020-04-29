package br.avaliatri.dtos;

import br.avaliatri.enums.Perfil;
import br.avaliatri.validators.UsuarioInsert;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@UsuarioInsert
public class UsuarioDTO {
    private Integer id;
    @NotNull(message = "Email nao pode esta nulo")
    @Email(message = "Insira um email valido")
    private String email;
    @NotNull(message = "Senha nao pode ser nula")
    @Length(min = 8, message = "A senha tem que ter mais que 8 digitos")
    private String password;
    private String name;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private Boolean is_active;
    private Integer qtd_provas_criadas;
    @JsonIgnore private Integer tipo_usuario = 1;
    private List<String> perfil = Arrays.asList(Perfil.ALUNO.getRole());
}
