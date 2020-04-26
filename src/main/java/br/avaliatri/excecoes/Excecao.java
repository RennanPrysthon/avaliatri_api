package br.avaliatri.excecoes;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Data
public class Excecao extends Exception{
    private List<String> erros;
    private Long timestamp;
    private Integer status;

    public Excecao(List<String> erros, Long timestamp, Integer status) {
        super("Erro na requisiçao");
        this.erros = erros;
        this.timestamp = timestamp;
        this.status = status;
    }
    public Excecao(List<String> erros) {
        super("Erro na requisicao");
        this.erros = erros;
        this.status = HttpStatus.BAD_REQUEST.value();
        this.timestamp = System.currentTimeMillis();
    }
    public Excecao(String error) {
        this(Collections.singletonList(error));
    }

    public Excecao(String error, HttpStatus status) {
        this(Collections.singletonList(error));
        this.status = status.value();
    }
}
