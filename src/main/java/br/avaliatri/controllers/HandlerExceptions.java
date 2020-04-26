package br.avaliatri.controllers;

import br.avaliatri.excecoes.Excecao;
import br.avaliatri.excecoes.ResponseError;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class HandlerExceptions {
    @InitBinder
    private void activateDirectFieldAccess(DataBinder dataBinder) {
        dataBinder.initDirectFieldAccess();
    }

    @ExceptionHandler(Excecao.class)
    public ResponseEntity<ResponseError> erro(Excecao e, HttpServletRequest request){
        ResponseError err = new ResponseError();
        err.setErros(e.getErros());
        err.setStatus(e.getStatus());
        err.setTimestamp(e.getTimestamp());
        return ResponseEntity.status(e.getStatus()).body(err);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
        ResponseError err = new ResponseError();
        err.setTimestamp(System.currentTimeMillis());
        err.setStatus(401);
        for (FieldError x: e.getBindingResult().getFieldErrors()) {
            err.getErros().add(x.getField() + ": " +x.getDefaultMessage());
        }

        return ResponseEntity.status(err.getStatus()).body(err);
    }

}
