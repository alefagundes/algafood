package com.aledev.algafood.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

//@JsonRootName("gastronomia") //eh possivel definir o nome da tag pai ao qual os dados serao encaminhados
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cozinha {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    //@JsonIgnore eh possivel nao enviar o campo na response
    @Column(nullable = false)
    //@JsonProperty("titulo") //possivel definir como sera esse nome do campo no json response
	private String nome;

    public Cozinha() {
    }

    public Cozinha(String nome) {
        this.nome = nome;
    }

	
}
