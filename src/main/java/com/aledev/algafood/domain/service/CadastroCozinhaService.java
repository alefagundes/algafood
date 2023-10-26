package com.aledev.algafood.domain.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.aledev.algafood.domain.exceptions.EntidadeEmUsoException;
import com.aledev.algafood.domain.exceptions.EntidadeNaoEncontradaException;
import com.aledev.algafood.domain.model.Cozinha;
import com.aledev.algafood.domain.repository.CozinhasRepository;

@Service
public class CadastroCozinhaService {
    @Autowired
    private CozinhasRepository cozinhaRepository;

    public Cozinha salvar(Cozinha cozinha){
        return cozinhaRepository.save(cozinha);
    }

    public void remove(Long id){
       try {
        if(!cozinhaRepository.existsById(id)){
            throw new EntidadeNaoEncontradaException(
			            String.format("N�o existe um cadastro de cozinha com c�digo %d", id));
        }
        cozinhaRepository.deleteById(id);
       }catch(DataIntegrityViolationException e){
        throw new EntidadeEmUsoException(String.format("Cozinha de c�digo %d n�o pode ser removida, pois est� em uso", id));
       }
    }
}
