package com.aledev.algafood.domain.exceptions;

public class RestauranteNaoEncontradoException extends EntidadeNaoEncontradaException{
    public RestauranteNaoEncontradoException(String message){
        super(message);
    }

    public RestauranteNaoEncontradoException(Long id){
        this(String.format("Não existe um cadastro de estado com código %d", id));
    }
    
}   
