package br.avaliatri.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlternativaDTO {
    private Integer id;
    private String Imagem;
    @NotNull(message = "Insira o id da questao")
    private Integer questao;
    private String alternativa;
    @NotNull(message = "O texto da alternativa nao pode ser nulo")
    @Length(min = 10, message = "Insira um texto maior para a alternativa")
    private String texto;
    private String created_at;
    private String updated_at;
    private String deleted_at;
}
