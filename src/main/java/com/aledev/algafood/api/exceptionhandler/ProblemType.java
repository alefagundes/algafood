package com.aledev.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
    DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
    ERRO_DE_SISTEMA("/erro-de-sistema", "Erro inesperado"),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parametro invalido"),
    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensivel"),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
    ERRO_NEGOCIO("/erro-negocio", "Regra de negócio violada");

    private String title;
    private String uri;

    ProblemType(String path, String title){
        this.title = title;
        this.uri = "http://aledev.com.br" + path;
    }
}
