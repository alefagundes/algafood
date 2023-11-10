package com.aledev.algafood.domain.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.aledev.algafood.domain.exceptions.CozinhaNaoEncontradaException;
import com.aledev.algafood.domain.exceptions.EntidadeEmUsoException;
import com.aledev.algafood.domain.exceptions.EntidadeNaoEncontradaException;
import com.aledev.algafood.domain.model.Cozinha;
import com.aledev.algafood.domain.repository.CozinhasRepository;

@Service
public class CadastroCozinhaService {
    private static final String MSG_COZINHA_EM_USO = "Cozinha de código %d não pode ser removida, pois está em uso";

    @Autowired
    private CozinhasRepository cozinhaRepository;

    public Cozinha salvar(Cozinha cozinha){
        return cozinhaRepository.save(cozinha);
    }

    public void remove(Long id){
       try {
        if(!cozinhaRepository.existsById(id)){
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND,  String.format("Não existe um cadastro de cozinha com código %d", id));
            throw new CozinhaNaoEncontradaException(id);
        }
        cozinhaRepository.deleteById(id);
       }catch(DataIntegrityViolationException e){
        throw new EntidadeEmUsoException(String.format(MSG_COZINHA_EM_USO, id));
       }
    }

    public Cozinha buscarOuFalhar(Long id){
        return cozinhaRepository.findById(id).orElseThrow(() -> 
        new CozinhaNaoEncontradaException(id));
    }
}
