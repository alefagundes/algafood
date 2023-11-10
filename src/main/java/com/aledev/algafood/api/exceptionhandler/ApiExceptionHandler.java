package com.aledev.algafood.api.exceptionhandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.aledev.algafood.domain.exceptions.EntidadeEmUsoException;
import com.aledev.algafood.domain.exceptions.EntidadeNaoEncontradaException;
import com.aledev.algafood.domain.exceptions.NegocioException;

@ControllerAdvice //focamos os pontos de tratamento de exceptions de todo o projto atraves desse controlador
public class ApiExceptionHandler{

    
    @ExceptionHandler(EntidadeNaoEncontradaException.class) //defino um metodo capaz de substituir o stack trace de uma requisicao com erro, esse mapeamento
    //ocorre para a exception que eh defina.
    public ResponseEntity<?> tratarEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e){
        //builder constroi o objeto para mim  de uma forma mais limpa de se ver
        Problema problema = Problema.builder().dataHome(LocalDateTime.now()).message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);
    }

    @ExceptionHandler(EntidadeEmUsoException.class) 
    public ResponseEntity<?> tratarEntidadeEmUsoException(EntidadeEmUsoException e){
        Problema problema = Problema.builder().dataHome(LocalDateTime.now()).message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problema);
    }

    @ExceptionHandler(NegocioException.class) 
    public ResponseEntity<?> tratarNegocioException(NegocioException e){
        Problema problema = Problema.builder().dataHome(LocalDateTime.now()).message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> tratarContentTypeDiferenteDeJson(){
        Problema problema = Problema.builder()
        .dataHome(LocalDateTime.now()).message("O tipo de mídia não é aceito").build();
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(problema);
    }
    
}
