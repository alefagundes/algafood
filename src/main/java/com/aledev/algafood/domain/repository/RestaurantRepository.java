package com.aledev.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aledev.algafood.domain.model.Restaurante;

@Repository
public interface RestaurantRepository  extends JpaRepository<Restaurante, Long>{
    
}
