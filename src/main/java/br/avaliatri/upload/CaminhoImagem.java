package br.avaliatri.upload;

import lombok.Data;

@Data
public class CaminhoImagem {
    private String caminho_arquivo;
    public CaminhoImagem(String caminho_arquivo) {
        this.caminho_arquivo = caminho_arquivo;
    }
}
