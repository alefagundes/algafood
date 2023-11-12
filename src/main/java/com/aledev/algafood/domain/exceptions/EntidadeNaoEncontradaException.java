package com.aledev.algafood.domain.exceptions;

public abstract class EntidadeNaoEncontradaException extends NegocioException {

	public EntidadeNaoEncontradaException(String mensagem){
		super(mensagem);
	}
	
}