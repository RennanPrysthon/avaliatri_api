package br.avaliatri.dtos;

import br.avaliatri.dtos.util.QuestaoRespondidaDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProvaRespondidaDTO {
    private Integer id;
    private String respondida_em;

    private String observacao;
    @NotNull(message = "Insira o id do usuario")private Integer usuario;

    private List<QuestaoRespondidaDTO> questoes_respondidas;
    private Integer nota;
    private Integer quantidade_questoes = 0;
    private ProvaDTO prova;
}
