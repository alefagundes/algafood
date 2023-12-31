package com.aledev.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aledev.algafood.domain.model.Restaurante;

@Repository //perceba que agora nao erdamos mais de jparepository e sim de customrepository
public interface RestaurantRepository extends CustomJpaRepository<Restaurante, Long>, RestaurantRepositoryImplQueries, JpaSpecificationExecutor<Restaurante>{

    List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

    //List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinhaId);

    @Query("from Restaurante r join fetch r.cozinha join fetch r.formasPagamento")
    List<Restaurante> findAll();

    //@Query("from Restaurante where nome like %:nome% and cozinha.id = :id")
    List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinhaId); //param para fazer o bind do valor do metodo para o valor na quert jpql.

    Optional<Restaurante> findFirstByNomeContaining(String nome);

    //buscar os dois primeiros com a que contaim na string recebida no metodo com a palavra reservada de Top
    List<Restaurante> findTop2ByNomeContaining(String nome);
}
