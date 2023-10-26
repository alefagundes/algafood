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
import com.aledev.algafood.domain.model.Estado;
import com.aledev.algafood.domain.repository.EstadoRepository;
import com.aledev.algafood.domain.service.CadastroEstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoController {
    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService estadoService;

    @GetMapping
    public ResponseEntity<List<Estado>> listar() {
        return ResponseEntity.status(HttpStatus.OK).body(estadoRepository.findAll());
    }


    @GetMapping("/{estadoId}")
    public ResponseEntity<Estado> getById(@PathVariable("estadoId") Long id){
        Optional<Estado> estado = estadoRepository.findById(id);
        if(estado.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(estadoRepository.findById(id).get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Estado> salvar(@RequestBody Estado estado){
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoService.salvar(estado));
    }

    @PutMapping("/{estadoId}")
	public ResponseEntity<Estado> atualizar(@PathVariable Long estadoId, @RequestBody Estado estadoResquest) {

		Optional<Estado> estadoAtual = estadoRepository.findById(estadoId);
		if (estadoAtual.isPresent()) {

			BeanUtils.copyProperties(estadoResquest, estadoAtual.get(), "id");
			//estadoAtual = estadoService.salvar(estadoAtual);
			return ResponseEntity.ok(estadoRepository.save(estadoAtual.get()));
		}
		return ResponseEntity.notFound().build();
	}
    

    @DeleteMapping("/{estadoId}")
    public ResponseEntity<?> remove(@PathVariable("estadoId") Long id){
        try{
            estadoService.remover(id);
            return ResponseEntity.noContent().build();
        }catch(EntidadeEmUsoException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch(EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }
    }

}
