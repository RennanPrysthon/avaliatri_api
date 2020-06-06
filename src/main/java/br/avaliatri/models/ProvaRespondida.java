package br.avaliatri.models;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity(name = "ProvaRespondida")
@Table(name = "prova_respondida")
public class ProvaRespondida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer qtd_questoes;
    private Integer nota;

    @OneToMany(
        mappedBy = "provaRespondida",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<QuestaoRespondida> questoes = new ArrayList<>();
    private Date respondida_em;
    private String observacao = "";
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prova_id")
    private Prova prova;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public void addQuestao(QuestaoRespondida questaoRespondida) {
        this.getQuestoes().add(questaoRespondida);
        questaoRespondida.setProvaRespondida(this);
    }

    public void removeQuestao(QuestaoRespondida questaoRespondida) {
        this.getQuestoes().remove(questaoRespondida);
        questaoRespondida.setProvaRespondida(null);
    }

}
