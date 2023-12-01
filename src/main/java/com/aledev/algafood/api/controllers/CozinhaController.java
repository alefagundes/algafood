package com.aledev.algafood.api.controllers;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aledev.algafood.domain.model.Cozinha;
import com.aledev.algafood.domain.repository.CozinhasRepository;
import com.aledev.algafood.domain.service.CadastroCozinhaService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhasRepository cozinhaRepository;

    @Autowired
    private CadastroCozinhaService cozinhaService;

    @GetMapping
    public ResponseEntity<List<Cozinha>> listar(){
        return ResponseEntity.status(HttpStatus.OK).body(cozinhaRepository.findAll());
    }

    @GetMapping("/{cozinhaId}")
    public Cozinha getById(@PathVariable("cozinhaId") Long id) {
        return cozinhaService.buscarOuFalhar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cozinha salvar(@RequestBody @Valid Cozinha cozinha) {
        return cozinhaService.salvar(cozinha);
    }

    @PutMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> atualizarCozinha(@PathVariable("cozinhaId") Long id, @RequestBody @Valid Cozinha cozinhaBody){
        Cozinha cozinhaAtual = cozinhaService.buscarOuFalhar(id);
        BeanUtils.copyProperties(cozinhaBody, cozinhaAtual, "id"); //copia as propriedades do primeiro objeto para o segundo
        // objeto do metodo, o terceiro parametro da funcao e as propriedades que deven ser ignoradas.
        return ResponseEntity.status(HttpStatus.OK).body(cozinhaService.salvar(cozinhaAtual));
    } 

    @DeleteMapping("/{cozinhaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable("cozinhaId") Long id){
        cozinhaService.remove(id);
    }
}
