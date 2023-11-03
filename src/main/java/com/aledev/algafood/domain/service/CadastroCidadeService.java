package com.aledev.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.aledev.algafood.domain.exceptions.EntidadeEmUsoException;
import com.aledev.algafood.domain.exceptions.EntidadeNaoEncontradaException;
import com.aledev.algafood.domain.model.Cidade;
import com.aledev.algafood.domain.model.Estado;
import com.aledev.algafood.domain.repository.CidadeRepository;
import com.aledev.algafood.domain.repository.EstadoRepository;

@Service
public class CadastroCidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    public Cidade salvar(Cidade cidade) {
        Optional<Estado> estado = estadoRepository.findById(cidade.getEstado().getId());
        if(estado.isEmpty()){
            throw new EntidadeNaoEncontradaException(String.format("Nao ha estado cadastrado para o codigo %d", cidade.getEstado().getId()));
        }
        cidade.setEstado(estado.get());
        return cidadeRepository.save(cidade);
    }

    public void remove(Long id){
       try {
        cidadeRepository.deleteById(id);
       
       }catch(EmptyResultDataAccessException e){
            throw new EntidadeNaoEncontradaException(String.format("Cidade de codigo %d nao encontrada", id));
       }catch(DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format("Cidade de codigo %d nao pode ser excluida pois esta em uso.", id));
       }
    }
}
