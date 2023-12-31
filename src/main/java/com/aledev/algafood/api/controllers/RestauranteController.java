package com.aledev.algafood.api.controllers;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aledev.algafood.core.validation.ValidacaoException;
import com.aledev.algafood.domain.exceptions.CozinhaNaoEncontradaException;
import com.aledev.algafood.domain.exceptions.EntidadeNaoEncontradaException;
import com.aledev.algafood.domain.exceptions.NegocioException;
import com.aledev.algafood.domain.model.Restaurante;
import com.aledev.algafood.domain.repository.RestaurantRepository;
import com.aledev.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestaurantRepository restauranteRepository;
    
    @Autowired
    private CadastroRestauranteService restauranteService;

    @Autowired
    private SmartValidator validator;

    @GetMapping
    public ResponseEntity<List<Restaurante>> listar(){
        return ResponseEntity.status(HttpStatus.OK).body(restauranteRepository.findAll());
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<Restaurante> getById(@PathVariable("restauranteId") Long id){
        Restaurante restaurante = restauranteService.buscarOuFalhar(id);
        return ResponseEntity.status(HttpStatus.OK).body(restaurante);
    }

    @PostMapping //? -> pype card, tipo coringa, diz que o metodo nesse caso vai retornar um response entity de alguma coisa.
    public ResponseEntity<?> salvar(@RequestBody @Valid Restaurante restaurante) {
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(restauranteService.salvar(restaurante));
        }catch(CozinhaNaoEncontradaException e){
            throw new NegocioException(e.getMessage());
        }
    }

    
    @PutMapping("/{restauranteId}")
    public ResponseEntity<?> atualizar(@PathVariable("restauranteId") Long id, @RequestBody @Valid Restaurante restaurante){
        Restaurante restauranteAtual = restauranteService.buscarOuFalhar(id);
        BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro", "produtos");
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restauranteService.salvar(restauranteAtual));
        }catch(CozinhaNaoEncontradaException e){
            throw new NegocioException(e.getMessage());
        }
    }
    
    @PatchMapping("/{restauranteId}")
    public ResponseEntity<?> atualizarParcial(@PathVariable("restauranteId") Long id, @RequestBody Map<String, 
           Object> campos, HttpServletRequest request){
       Restaurante restauranteAtual = restauranteService.buscarOuFalhar(id);
       merge(campos, restauranteAtual, request);
       validate(restauranteAtual, "restaurante");
       try {
           return atualizar(id, restauranteAtual);
       }catch(EntidadeNaoEncontradaException e){
        throw new NegocioException(e.getMessage());
       }
    }

    private void validate(Restaurante restaurante, String objectName) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
        validator.validate(restaurante, bindingResult);

        if(bindingResult.hasErrors()){
            throw new ValidacaoException(bindingResult);
        }
    }

    private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
      ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
      try {
        ObjectMapper objectMapper = new ObjectMapper(); //classe reponsavel por seralizar objetos java em json e json em java
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

        dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
           //Refletion do spring eh o meio possivel de alterar objetos em tempo de execusao.
           //desse modo estamos percorrendo o map camposOrigem e setando o valor das propriedades.
           Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
           field.setAccessible(true);
           Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

           ReflectionUtils.setField(field, restauranteDestino, novoValor);
        });
      }catch(IllegalArgumentException e){ //relancamos a illegalArgumentException para a HttpMessageNotReadableException que e tratada no ControllerAdvice
        Throwable rootCause = ExceptionUtils.getRootCause(e);
        throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
      }  
    } 
}
