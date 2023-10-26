package com.aledev.algafood.domain.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class Restaurante {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private BigDecimal taxaFrete;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Cozinha cozinha; 

    public Restaurante() {
    }

    public Restaurante(String nome, BigDecimal taxaFrete) {
        this.nome = nome;
        this.taxaFrete = taxaFrete;
    }
    
}
