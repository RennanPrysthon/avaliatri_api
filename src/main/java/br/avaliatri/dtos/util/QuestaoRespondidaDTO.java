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
    private List<AlternativaDTO> alternativas;
    private Boolean is_correta;

    public QuestaoRespondidaDTO() {

    }
    public QuestaoRespondidaDTO(Integer id, String alternativa_usuario, List<Alternativa> alternativas, boolean is_correta) {
        this.questao = id;
        this.alternativa_usuario = alternativa_usuario;
        this.alternativas = AlternativaService.convertEntityListToDtoList(alternativas);
        this.is_correta = is_correta;
    }
}
