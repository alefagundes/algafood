package com.aledev.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aledev.algafood.domain.exceptions.RestauranteNaoEncontradoException;
import com.aledev.algafood.domain.model.Cozinha;
import com.aledev.algafood.domain.model.Restaurante;
import com.aledev.algafood.domain.repository.CozinhasRepository;
import com.aledev.algafood.domain.repository.RestaurantRepository;


@Service
public class CadastroRestauranteService {

    @Autowired
    private CozinhasRepository cozinhasRepository;

    @Autowired
    private RestaurantRepository restauranteRepository;

    public Restaurante salvar(Restaurante restaurante){
        Long idCozinha = restaurante.getCozinha().getId();
        Cozinha cozinha = cozinhasRepository.findById(idCozinha).orElseThrow(() -> 
                new RestauranteNaoEncontradoException(idCozinha));
        //caso nao encontra nada buscando pelo id recebido, dispara a excessao informada no orElseThrow().
        
        restaurante.setCozinha(cozinha);
        return restauranteRepository.save(restaurante);
    }

    public Restaurante buscarOuFalhar(Long id) {
        return restauranteRepository.findById(id).orElseThrow(() -> 
        new RestauranteNaoEncontradoException(id));
    }
    
}
