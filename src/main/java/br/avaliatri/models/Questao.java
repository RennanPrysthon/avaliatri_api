package br.avaliatri.models;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String enunciado;

    @ManyToOne
    @JoinColumn(name ="iamgem_id")
    private Imagem imagem;

    @OneToMany(mappedBy = "questao", cascade = CascadeType.REMOVE)
    private List<Alternativa> alternativas = new ArrayList<>();

    @OneToMany(mappedBy = "questao", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<QuestaoRespondida> questaoesRespondidas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prova_id")
    private Prova prova;
    private String alternativa_correta;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;
}
