package br.avaliatri.dtos;


import br.avaliatri.dtos.util.AlternativaValor;
import br.avaliatri.validators.AlternativaInsert;
import br.avaliatri.validators.QuestaoInsert;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@QuestaoInsert
public class QuestaoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer prova;

    private String Imagem = "";
    private List<AlternativaDTO> alternativas = new ArrayList<>();
    @NotEmpty(message = "Enunciado obrigatorio") private String enunciado;

    private String alternativa_correta = "A";
    @AlternativaInsert private AlternativaDTO alternativaA;
    @AlternativaInsert private AlternativaDTO alternativaB;
    @AlternativaInsert private AlternativaDTO alternativaC;
    @AlternativaInsert private AlternativaDTO alternativaD;
    @AlternativaInsert private AlternativaDTO alternativaE;

    private String created_at;
    private String updated_at;
    private String deleted_at;
}
