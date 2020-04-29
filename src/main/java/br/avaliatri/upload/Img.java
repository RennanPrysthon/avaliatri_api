package br.avaliatri.upload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
@Data
@NoArgsConstructor
public class Img {
    private String nomeCompleto;
    private String nomeSemExtensao;
    private String extensao;
    private String pastaImagem;
    private File arquivo;
}
