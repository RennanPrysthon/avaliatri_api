package br.avaliatri.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultadoDTO {
    private Integer quantidade_provas_respondidas;
    private String user_name;
    private String user_email;
    private List<ProvaRespondidaDTO> provas_respondidas;
}
