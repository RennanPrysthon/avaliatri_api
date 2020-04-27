package br.avaliatri.models;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
public class ProvaRespondida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer qtd_questoes;
    private Integer nota;

    @OneToMany
    private List<QuestaoRespondida> questoes;
    private Date respondida_em;
    private String observacao;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prova_id")
    private Prova prova;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
