package br.avaliatri.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Imagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Questao> questoes;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Alternativa> alternativa;
}
