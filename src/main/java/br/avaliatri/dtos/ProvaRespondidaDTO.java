package br.avaliatri.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Data
public class ProvaRespondidaDTO {
    private Integer id;
    private HashMap<Integer, AlternativaDTO> questoes_respondida = new HashMap<>();
    private HashMap<Integer, AlternativaDTO> questoes_resultado = new HashMap<>();
    private List<QuestaoDTO> questoes_finais = new ArrayList<>();
    private Integer quantidade_questoes = 0;
    private String respondida_em;
    private BigDecimal nota = BigDecimal.valueOf(0.0);
    private String observacao;
    private Integer prova;
    private Integer usuario;
}
