package br.avaliatri.dtos;

import br.avaliatri.dtos.util.QuestaoRespondidaDTO;
import br.avaliatri.validators.ProvaInsert;
import br.avaliatri.validators.QuestaoInsert;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ProvaInsert
public class ProvaDTO {
    private Integer id;
    private List<QuestaoDTO> questoes = new ArrayList<>();
    private Integer qtd_questoes = 0;
    private Integer usuario;
    private String criado_por;
    private String title;
    private String description;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private Boolean is_published;
    private Boolean is_activated;
}
