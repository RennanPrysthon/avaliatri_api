package br.avaliatri.dtos.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlternativaValor {
    private String alternativa;
    private String valor;
    private String imagem;

    public AlternativaValor(String a, String texto) {
        this.alternativa = a;
        this.valor = texto;
        this.imagem = "";
    }

    public AlternativaValor(String alternativa, String valor, String imagem) {
        this.alternativa = alternativa;
        this.valor = valor;
        this.imagem = imagem;
    }

    public AlternativaValor() {
    }
}
