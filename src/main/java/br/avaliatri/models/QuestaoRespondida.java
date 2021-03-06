package br.avaliatri.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
public class QuestaoRespondida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;
    private String alternativa_usuario;
    private Boolean is_correta;

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "questao_id")
    private Questao questao;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "prova_respondida_id")
    private ProvaRespondida provaRespondida;

    public QuestaoRespondida(Integer id, Questao questao_salva, String alternativa_usuario,boolean b) {
        this.id = id;
        this.questao = questao_salva;
        this.alternativa_usuario = alternativa_usuario;
        this.is_correta = b;
    }
}
