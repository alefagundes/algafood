package com.aledev.algafood.domain.model;

import com.aledev.algafood.Groups;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

//@JsonRootName("States") definir nome para o agregado por um todo (model name)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class Estado {

    //@JsonProperty("estadoId") definir nome para a propriedade
    @NotNull(groups = Groups.EstadoId.class)
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    //@JsonIgnore iganorar nome estado
    @NotBlank
    @Column(nullable = false)
    private String nome;
    
}
