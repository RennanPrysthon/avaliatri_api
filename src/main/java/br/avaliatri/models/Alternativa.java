package br.avaliatri.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "imagem_id")
    private Imagem imagem;
    private Boolean temImagem = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questao_id")
    @JsonIgnore
    private Questao questao;
    private String opcao;
    private String texto;

    public Alternativa(String opcao, String texto) {
        this.opcao = opcao;
        this.texto = texto;
    }
}
