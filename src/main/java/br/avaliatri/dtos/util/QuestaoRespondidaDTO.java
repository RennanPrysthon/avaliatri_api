package br.avaliatri.dtos.util;

import br.avaliatri.dtos.AlternativaDTO;
import br.avaliatri.models.Alternativa;
import br.avaliatri.services.AlternativaService;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestaoRespondidaDTO {
    @NotNull(message = "Insira o id da questao") private Integer questao;
    @NotNull(message = "Insira a alternativa") private String alternativa_usuario;
    private String alternativaA;
    private String alternativaB;
    private String alternativaC;
    private String alternativaD;
    private String alternativaE;
    private Boolean is_correta;
    private String enunciado;
}
