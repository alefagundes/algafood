package com.aledev.algafood.domain.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aledev.algafood.domain.model.Cozinha;

@Repository
public interface CozinhasRepository extends JpaRepository<Cozinha, Long> {

    //findBy -> eh um prefixo utiizado e aceito pelo JpaRepository no qual eh utilizado para fazer buscas por registros no qual a condicao
    //esteja atrelada ao nome da propriedade da sua entidade.
    List<Cozinha> findByNome(String nome);
    
}
