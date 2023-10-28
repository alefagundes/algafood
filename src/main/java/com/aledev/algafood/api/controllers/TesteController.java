package com.aledev.algafood.api.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aledev.algafood.domain.model.Cozinha;
import com.aledev.algafood.domain.model.Restaurante;
import com.aledev.algafood.domain.repository.CozinhasRepository;
import com.aledev.algafood.domain.repository.RestaurantRepository;

@RestController
@RequestMapping("/teste")
public class TesteController {

    @Autowired
    private CozinhasRepository cozinhasRepository;
    
    @Autowired
    private RestaurantRepository restauranteRepository;

    @GetMapping("/cozinhas/por-nome")
    public ResponseEntity<List<Cozinha>> buscarPorNome(@RequestParam("nome") String nome){
        return ResponseEntity.ok(cozinhasRepository.findByNomeContaining(nome));
    } 

     @GetMapping(value="/por-taxa-frete")
    public ResponseEntity<List<Restaurante>> restaurantePorTaxaFrete(@RequestParam("txIni") BigDecimal taxaInicial, @RequestParam("txFinal") BigDecimal taxaFinal) {
        return ResponseEntity.ok().body(restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal));
    }

    @GetMapping(value="/restaurante/nome-id")
    public ResponseEntity<List<Restaurante>> restauranteByNomeAndCozinhaId(@RequestParam String nome, @RequestParam Long cozinhaId) {
        return ResponseEntity.ok().body(restauranteRepository.findByNomeContainingAndCozinhaId(nome, cozinhaId));
    }

    @GetMapping("/restaurantes/first-name")
    public ResponseEntity<Optional<Restaurante>> primeiroRestauranteByNome(@RequestParam String nome){
        return ResponseEntity.ok().body(restauranteRepository.findFirstByNomeContaining(nome));
    }
    
    @GetMapping("/restaurantes/top-nome")
    public ResponseEntity<List<Restaurante>> buscarTop2RestaurantesComNome(@RequestParam String nome){
        return ResponseEntity.ok(restauranteRepository.findTop2ByNomeContaining(nome));
    }

    @GetMapping("/cozinhas/exists")
    public ResponseEntity<Boolean> findByNomeContainingExists(@RequestParam String nome){
        return ResponseEntity.ok(cozinhasRepository.existsByNomeContaining(nome));
    }

     @GetMapping("cozinhas/counter")
    ResponseEntity<Integer> counterNomeCozinhasContaining(@RequestParam("name") String nome){
        return ResponseEntity.status(HttpStatus.OK).body(cozinhasRepository.countByNomeContaining(nome));
    }

}
