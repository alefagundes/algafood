package com.aledev.algafood.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aledev.algafood.domain.model.Cozinha;
import com.aledev.algafood.domain.repository.CozinhasRepository;

@RestController
@RequestMapping("/teste")
public class TesteController {

    @Autowired
    private CozinhasRepository cozinhasRepository;

    @GetMapping("/cozinhas/por-nome")
    public ResponseEntity<List<Cozinha>> buscarPorNome(@RequestParam("nome") String nome){
        return ResponseEntity.ok(cozinhasRepository.findByNome(nome));
    } 
}
