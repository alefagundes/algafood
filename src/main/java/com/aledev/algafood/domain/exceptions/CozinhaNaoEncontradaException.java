package com.aledev.algafood.domain.exceptions;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException{

    public CozinhaNaoEncontradaException(String message){
        super(message);
    }

    public CozinhaNaoEncontradaException(Long id){
        this(String.format("Não existe um cadastro de cozinha com código %d", id));
    }
    
}
