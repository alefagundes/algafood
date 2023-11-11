package com.aledev.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.aledev.algafood.domain.exceptions.CidadeNaoEncontradaException;
import com.aledev.algafood.domain.exceptions.EntidadeEmUsoException;
import com.aledev.algafood.domain.model.Cidade;
import com.aledev.algafood.domain.model.Estado;
import com.aledev.algafood.domain.repository.CidadeRepository;

@Service
public class CadastroCidadeService {

    private static final String MSG_CIDADE_EM_USO = "Cidade de código %d não pode ser removida, pois está em uso";

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroEstadoService estadoService;

    public Cidade salvar(Cidade cidade) {
        Estado estado = estadoService.buscarOuFalhar(cidade.getEstado().getId());
        cidade.setEstado(estado);
        return cidadeRepository.save(cidade);
    }

    public void remove(Long id){
       try {
        if(!cidadeRepository.existsById(id)){
            throw new CidadeNaoEncontradaException(id);
        }
        cidadeRepository.deleteById(id);
        
       }catch(DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format(MSG_CIDADE_EM_USO, id));
       }
    }

    public Cidade buscarOuFalhar(Long id){
        return cidadeRepository.findById(id).orElseThrow(() -> 
        new CidadeNaoEncontradaException(id));
    }

}
