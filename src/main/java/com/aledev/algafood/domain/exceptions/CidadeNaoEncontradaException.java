package com.aledev.algafood.domain.exceptions;

public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException {

    public CidadeNaoEncontradaException(String mensagem){
        super(mensagem);
    }
    
    public CidadeNaoEncontradaException(Long id){
        this(String.format("Não existe um cadastro de cidade com código %d", id));
    }
}