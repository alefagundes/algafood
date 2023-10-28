package com.aledev.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.aledev.algafood.domain.model.Restaurante;

public interface RestaurantRepositoryImplQueries {

    List<Restaurante> find(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal);

}