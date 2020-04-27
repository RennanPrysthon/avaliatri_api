package br.avaliatri.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Alternativa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "imagem_id")
    private Imagem imagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questao_id")
    private Questao questao;
    private String opcao;
    private String texto;

    public Alternativa(String opcao, String texto) {
        this.opcao = opcao;
        this.texto = texto;
    }
}
