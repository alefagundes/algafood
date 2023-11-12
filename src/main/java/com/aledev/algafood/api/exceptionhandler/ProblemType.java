package com.aledev.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensivel"),
    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade nao encontrada"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
    ERRO_NEGOCIO("/erro-negocio", "Regra de negócio violada");

    private String title;
    private String uri;

    ProblemType(String path, String title){
        this.title = title;
        this.uri = "http://aledev.com.br" + path;

    
    }
}
