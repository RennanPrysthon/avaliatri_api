package br.avaliatri.excecoes;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class ResponseError {
    private List<String> erros = new ArrayList<>();
    private Integer status;
    private Long timestamp;

    public ResponseError() {
    }

    public ResponseError(int status, String message, long timestamp) {
        status = status;
        erros = Collections.singletonList(message);
        timestamp = timestamp;
    }
}
