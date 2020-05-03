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
    @NotNull(message = "Insira o id do usuario") private Integer usuario;
    private String respondida_em;

    private String observacao;
    private List<QuestaoRespondidaDTO> questoes_respondidas;
    private Integer nota;
    private Integer quantidade_questoes = 0;
    private String titulo;
    private ProvaDTO prova;
    private String nome_usuario;
}
