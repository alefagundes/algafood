package com.aledev.algafood.api.controllers;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aledev.algafood.domain.exceptions.EntidadeNaoEncontradaException;
import com.aledev.algafood.domain.model.Restaurante;
import com.aledev.algafood.domain.repository.RestaurantRepository;
import com.aledev.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestaurantRepository restauranteRepository;
    
    @Autowired
    private CadastroRestauranteService restauranteService;

    @GetMapping
    public ResponseEntity<List<Restaurante>> listar(){
        return ResponseEntity.status(HttpStatus.OK).body(restauranteRepository.findAll());
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<Restaurante> getById(@PathVariable("restauranteId") Long id){
        Optional<Restaurante> restaurante = restauranteRepository.findById(id);
        if(restaurante.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(restaurante.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping //? -> pype card, tipo coringa, diz que o metodo nesse caso vai retornar um response entity de alguma coisa.
    public ResponseEntity<?> salvar(@RequestBody Restaurante restaurante) {
        try{
            restaurante = restauranteService.salvar(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
        }catch(EntidadeNaoEncontradaException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    @PutMapping("/{restauranteId}")
    public ResponseEntity<?> atualizar(@PathVariable("restauranteId") Long id, @RequestBody Restaurante restaurante){
        try{
            Optional<Restaurante> restauranteAtual = restauranteRepository.findById(id);
            if(restauranteAtual.isPresent()){
                BeanUtils.copyProperties(restaurante, restauranteAtual.get(), "id");
                restaurante = restauranteRepository.save(restauranteAtual.get());
                return ResponseEntity.status(HttpStatus.OK).body(restauranteAtual);
            }
            return ResponseEntity.notFound().build();
        }catch(EntidadeNaoEncontradaException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PatchMapping("/{restauranteId}")
    public ResponseEntity<?> atualizarParcial(@PathVariable("restauranteId") Long id, @RequestBody Map<String, Object> campos){
       Optional<Restaurante> restauranteAtual = restauranteRepository.findById(id);
       if(restauranteAtual.isEmpty()){
            return ResponseEntity.notFound().build();
       }
       merge(campos, restauranteAtual.get());
       return atualizar(id, restauranteAtual.get());
    }

    private Map<String, Object> merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
        ObjectMapper objectMapper = new ObjectMapper(); //classe reponsavel por seralizar objetos java em json e json em java
        Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
        System.out.println(restauranteOrigem);

        dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
           //Refletion do spring eh o meio possivel de alterar objetos em tempo de execusao.
           //desse modo estamos percorrendo o map camposOrigem e setando o valor das propriedades.
           Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
           field.setAccessible(true);
           Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

           ReflectionUtils.setField(field, restauranteDestino, novoValor);
        });
        return dadosOrigem;
    } 
}
