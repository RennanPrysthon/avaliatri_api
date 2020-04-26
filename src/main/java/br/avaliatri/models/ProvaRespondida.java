package br.avaliatri.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class ProvaRespondida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer qtd_questoes;
    private Map<Integer, String> questoes_respondida = new HashMap<>();
    private Map<Integer, String> questoes_resultado = new HashMap<>();
    private Date respondida_em;
    private BigDecimal nota;
    private String observacao;
    @ManyToOne(fetch = FetchType.LAZY)
    private Prova prova;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;
}
