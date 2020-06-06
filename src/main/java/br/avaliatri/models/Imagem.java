package br.avaliatri.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Imagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String caminhoArquivo;
    private String nomeArquivo;
    private String extensao;
    @OneToMany(mappedBy = "imagem", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Questao> questoes = new ArrayList<>();
    @OneToMany(mappedBy = "imagem", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Alternativa> alternativa = new ArrayList<>();

    public Imagem(String caminho_arquivo, String nomeSemExtensao, String extensao) {
        this.caminhoArquivo = caminho_arquivo;
        this.nomeArquivo = nomeSemExtensao;
        this.extensao = extensao;
    }
}
