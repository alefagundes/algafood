package com.aledev.algafood.api.controllers;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import com.aledev.algafood.domain.exceptions.EntidadeEmUsoException;
import com.aledev.algafood.domain.exceptions.EntidadeNaoEncontradaException;
import com.aledev.algafood.domain.model.Cidade;
import com.aledev.algafood.domain.repository.CidadeRepository;
import com.aledev.algafood.domain.service.CadastroCidadeService;

@RequestMapping("/cidades")
@RestController
public class ControllerCidade {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cidadeService;

    @GetMapping
    public ResponseEntity<List<Cidade>> listar() {
        return ResponseEntity.status(HttpStatus.OK).body(cidadeRepository.findAll());
    }
    
    @GetMapping("/{cidadeId}")
    public ResponseEntity<Cidade> getByid(@PathVariable("cidadeId") Long id){
        Optional<Cidade> cidade = cidadeRepository.findById(id);
        if(cidade.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(cidade.get());
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Cidade cidade) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(cidadeService.salvar(cidade));
        }catch(EntidadeNaoEncontradaException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    } 

    @PutMapping("/{cidadeId}")
    public ResponseEntity<?> atualizar(@PathVariable("cidadeId") Long id, @RequestBody Cidade cidade){
            Optional<Cidade> cidadeAtual = cidadeRepository.findById(id);
            if(cidadeAtual.isPresent()){
                BeanUtils.copyProperties(cidade, cidadeAtual.get(), "id");
                return ResponseEntity.ok(cidadeService.salvar(cidadeAtual.get()));
            }
            return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{cidadeId}")
    public ResponseEntity<?> remove(@PathVariable("cidadeId") Long id){
        try{
            cidadeService.remove(id);
            return ResponseEntity.noContent().build();
        }catch(EntidadeEmUsoException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch(EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }
    }
}
