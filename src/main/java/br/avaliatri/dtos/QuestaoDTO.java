package br.avaliatri.dtos;

import br.avaliatri.validators.QuestaoInsert;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@QuestaoInsert
public class QuestaoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer prova;
    private String imagem = "";
    private Boolean temImagem = false;
    private String textoApoio = "";
    private List<AlternativaDTO> alternativas;
    @NotEmpty(message = "Enunciado obrigatorio") private String enunciado;

    private String alternativa_correta = "A";
    private String alternativaA;
    private String alternativaB;
    private String alternativaC;
    private String alternativaD;
    private String alternativaE;
}
