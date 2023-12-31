package com.aledev.algafood.domain.model;

import com.aledev.algafood.core.validation.Groups;
import com.aledev.algafood.core.validation.Multiplo;
import com.aledev.algafood.core.validation.ValorZeroIncluiDescricao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ValorZeroIncluiDescricao(valorField = "taxaFrete", descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class Restaurante {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    //@TaxaFrete
    //@Multiplo(numero= 5)
    @NotNull
    @PositiveOrZero(message = "{TaxaFrete.invalida}") //a chave passada entre parenteses deve estar atrelada ao bean validation ao qual se refere para ser possivel acessar a msg
    @Column(nullable = false)
    private BigDecimal taxaFrete;

    //@JsonIgnoreProperties("hibernateLazyInitializer")//faz com que ignore o carregamento das cozinhas ate que seja necessario, quando necessario executa a consulta
    //@JsonIgnore
    @Valid //diz para o spring validar a propriedade e valida ate mesmo em cascata se for necessario
    @ConvertGroup(from = Default.class, to =  Groups.CozinhaId.class)
    @NotNull
    @ManyToOne //(fetch = FetchType.LAZY) //tudo que termina com one utiliza a estrategia eger loading e tudo que termina com many utiliza a estrategia leazy loading
    @JoinColumn(name = "cozinha_id", nullable = false)
    private Cozinha cozinha; 

    @JsonIgnore
    @Embedded //incorporando a classe endereco, basicamente eu digo que nao eh uma coluna e sim incorporacao
    private Endereco endereco;

    @JsonIgnore
    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime dataCadastro; //representa uma data local hora sem fuso horario 
    
    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime dataAtualizacao;
    
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "restaurante_forma_pagamento", joinColumns = @JoinColumn(name="restaurante_id"), 
    inverseJoinColumns = @JoinColumn(name="forma_pagamento_id"))
    private List<FormaPagamento> formasPagamento = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "restaurante")
    private List<Produto> produtos = new ArrayList<>();
}
