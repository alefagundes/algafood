package com.aledev.algafood.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.aledev.algafood.domain.repository.CustomJpaRepository;

import jakarta.persistence.EntityManager;

public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements CustomJpaRepository<T, ID> {

    private EntityManager menager;

    public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager){
        super(entityInformation, entityManager);
        this.menager = entityManager;
    }

    @Override
    public Optional<T> buscarPrimeiro() {
       var jpql = "from " + getDomainClass().getName();
       T entity = menager.createQuery(jpql, getDomainClass()).setMaxResults(1).getSingleResult();
       return Optional.ofNullable(entity);
    }
    
}
