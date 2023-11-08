package com.aledev.algafood.domain.exceptions;

//esta herdando o status da da resposta com base na EntidadeNaoEncontradaExcepetion
public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException{
    
    public EstadoNaoEncontradoException(String message){
        super(message);
    }
    
    public EstadoNaoEncontradoException(Long id){
        this(String.format("Não existe um cadastro de estado com código %d", id));
    }
}
