package br.avaliatri.models;

import br.avaliatri.dtos.util.AlternativaValor;
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
    private Imagem imagem;

    @ManyToOne(fetch = FetchType.LAZY)
    private Questao questao;
    private String opcao;
    private String texto;

    public Alternativa(String opcao, String texto) {
        this.opcao = opcao;
        this.texto = texto;
    }

    public Alternativa(AlternativaValor alternativa) {
        this.opcao = alternativa.getAlternativa();
        this.texto = alternativa.getValor();
    }
}
