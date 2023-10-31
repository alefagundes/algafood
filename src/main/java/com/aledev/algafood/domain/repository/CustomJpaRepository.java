package com.aledev.algafood.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID>{
    //com esses genericos utilizados T, ID a gente consegue repassar quando estanciado ussa classe,
    //basta passar a entidade e o tipo do id da entidade, sera possivel manter apenas uma classe de repositorio para todas as entidades.

    Optional<T> buscarPrimeiro();
}
