package com.aledev.algafood.domain.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND,  String.format("Não existe um cadastro de cozinha com código %d", id));
            throw new EntidadeNaoEncontradaException(
			            String.format("Não existe um cadastro de cozinha com código %d", id));
        }
        cozinhaRepository.deleteById(id);
       }catch(DataIntegrityViolationException e){
        throw new EntidadeEmUsoException(String.format("Cozinha de código %d n�o pode ser removida, pois está em uso", id));
       }
    }
}
