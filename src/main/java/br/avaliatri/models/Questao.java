package br.avaliatri.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.util.*;

@Entity
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String enunciado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="imagem_id")
    private Imagem imagem;
    private Boolean temImagem = false;
    private String textoApoio;

    @OneToMany(mappedBy = "questao", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Alternativa> alternativas = new ArrayList<>();

    @OneToMany(mappedBy = "questao", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<QuestaoRespondida> questaoesRespondidas = new ArrayList<>();

    @ManyToMany(mappedBy="questoes")
    @JsonIgnore
    private Set<Prova> provas = new HashSet<>();
    private String alternativa_correta;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;

    public Questao() {
    }

    public void addProva(Prova p) {
        this.getProvas().add(p);
        p.getQuestoes().add(this);
    }

    public void removeProva(Prova p) {
        this.getProvas().remove(p);
        p.getQuestoes().remove(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
    }

    public Boolean getTemImagem() {
        return temImagem;
    }

    public void setTemImagem(Boolean temImagem) {
        this.temImagem = temImagem;
    }

    public String getTextoApoio() {
        return textoApoio;
    }

    public void setTextoApoio(String textoApoio) {
        this.textoApoio = textoApoio;
    }

    public List<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<Alternativa> alternativas) {
        this.alternativas = alternativas;
    }

    public List<QuestaoRespondida> getQuestaoesRespondidas() {
        return questaoesRespondidas;
    }

    public void setQuestaoesRespondidas(List<QuestaoRespondida> questaoesRespondidas) {
        this.questaoesRespondidas = questaoesRespondidas;
    }

    public Set<Prova> getProvas() {
        return provas;
    }

    public void setProvas(Set<Prova> provas) {
        this.provas = provas;
    }

    public String getAlternativa_correta() {
        return alternativa_correta;
    }

    public void setAlternativa_correta(String alternativa_correta) {
        this.alternativa_correta = alternativa_correta;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }
}
