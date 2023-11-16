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
import org.springframework.web.bind.annotation.RestController;

import com.aledev.algafood.domain.exceptions.EntidadeNaoEncontradaException;
import com.aledev.algafood.domain.exceptions.EstadoNaoEncontradoException;
import com.aledev.algafood.domain.exceptions.NegocioException;
import com.aledev.algafood.domain.model.Cidade;
import com.aledev.algafood.domain.repository.CidadeRepository;
import com.aledev.algafood.domain.service.CadastroCidadeService;

import jakarta.validation.Valid;

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
        Cidade cidade = cidadeService.buscarOuFalhar(id);
        return ResponseEntity.status(HttpStatus.OK).body(cidade);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid Cidade cidade) {
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(cidadeService.salvar(cidade));
        }catch(EntidadeNaoEncontradaException e){
            throw new NegocioException(e.getMessage());
        }
    } 

    @PutMapping("/{cidadeId}")
	public Cidade atualizar(@PathVariable Long cidadeId, @RequestBody @Valid Cidade cidade) {
		Cidade cidadeAtual = cidadeService.buscarOuFalhar(cidadeId);
		BeanUtils.copyProperties(cidade, cidadeAtual, "id");
		try {
			return cidadeService.salvar(cidadeAtual);
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

    @DeleteMapping("/{cidadeId}")
    public void remove(@PathVariable("cidadeId") Long id){
        cidadeService.remove(id);    
    }
}
