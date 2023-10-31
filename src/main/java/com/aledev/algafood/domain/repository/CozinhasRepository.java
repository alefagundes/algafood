package com.aledev.algafood.domain.repository;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.aledev.algafood.domain.model.Cozinha;

@Repository
public interface CozinhasRepository extends CustomJpaRepository<Cozinha, Long> {

    //findBy -> eh um prefixo utiizado e aceito pelo JpaRepository no qual eh utilizado para fazer buscas por registros no qual a condicao
    //esteja atrelada ao nome da propriedade da sua entidade.
    //containing eh como so like do jpa referindo-se a propriedade da entidade ao qual sera feita a busca
    List<Cozinha> findByNomeContaining(String nome);
    
    Boolean existsByNomeContaining(String nome);

    Integer countByNomeContaining(String nome);
    
}
