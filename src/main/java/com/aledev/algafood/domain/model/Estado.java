package com.aledev.algafood.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

//@JsonRootName("States") definir nome para o agregado por um todo (model name)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class Estado {

    //@JsonProperty("estadoId") definir nome para a propriedade
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    //@JsonIgnore iganorar nome estado
    @Column(nullable = false)
    private String nome;
    
}
