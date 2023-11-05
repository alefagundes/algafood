package com.aledev.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.aledev.algafood.domain.exceptions.EntidadeEmUsoException;
import com.aledev.algafood.domain.exceptions.EntidadeNaoEncontradaException;
import com.aledev.algafood.domain.model.Estado;
import com.aledev.algafood.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {

    //no service a logica eh feita a nivel de classe o retorno e controller que utilizamos o retorno com entidade.

    private static final String MSG_ESTADO_NAO_ENCONTRADO = "Não existe estado cadastrado para o codigo %d";
    private static final String MSG_ESTADO_EM_USO = "O estado de codigo %d nao pode ser excluido pois esta em uso.";
    
    @Autowired
    private EstadoRepository estadoRepository;

    public Estado salvar(Estado estado){
        return estadoRepository.save(estado);
    }

    public void remover(Long id){
       try{
        estadoRepository.deleteById(id);
       }catch(EmptyResultDataAccessException e){
            throw new EntidadeNaoEncontradaException(String.format(MSG_ESTADO_NAO_ENCONTRADO, id));
       }catch(DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format(MSG_ESTADO_EM_USO, id));
       }
    }

    public Estado buscarOuFalhar(Long id){
        return estadoRepository.findById(id).orElseThrow(() -> 
        new EntidadeNaoEncontradaException(String.format(MSG_ESTADO_EM_USO, id)));
    }
}
