package com.aledev.algafood.api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import org.springframework.web.server.ResponseStatusException;

import com.aledev.algafood.domain.exceptions.EntidadeEmUsoException;
import com.aledev.algafood.domain.exceptions.EntidadeNaoEncontradaException;
import com.aledev.algafood.domain.model.Cozinha;
import com.aledev.algafood.domain.repository.CozinhasRepository;
import com.aledev.algafood.domain.service.CadastroCozinhaService;


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

    /* @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{cozinhaId}") // caso nao queira fazer o bind dessa variavel para id, 
    //basta definir a mesma com o mesmo nome no metodo.
    public ResponseEntity<Cozinha> getById(@PathVariable("cozinhaId") Long id){
        Cozinha cozinha = cozinhaRepository.getCozinhaById(id);
        //return ResponseEntity.status(HttpStatus.OK).body(cozinha);
        //return ResponseEntity.ok(cozinha);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "http://api.algafood.local:8080/cozinhas");
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }  */

    @GetMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> getById(@PathVariable("cozinhaId") Long id) {
        Optional<Cozinha> cozinha = cozinhaRepository.findById(id);
        if(cozinha.isPresent()){ //metodo isPresent() para validar se ha um elemento capturado polo optinal
            return ResponseEntity.ok(cozinha.get()); // esse get basicamente eh pq o optional eh um objteto complexo e se houver uma cozinha
            //dentro do optional de cozinha ela pode ser acessada atraves do get pegando o objeto de cozinha.
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cozinha salvar(@RequestBody Cozinha cozinha) {
        return cozinhaService.salvar(cozinha);
    }

    @PutMapping("/{cozinhaId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cozinha> atualizarCozinha(@PathVariable("cozinhaId") Long id, @RequestBody Cozinha cozinhaBody){
        Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(id);
        if(cozinhaAtual.isPresent()){
            BeanUtils.copyProperties(cozinhaBody, cozinhaAtual.get(), "id"); //copia as propriedades do primeiro objeto para o segundo
            // objeto do metodo, o terceiro parametro da funcao e as propriedades que deven ser ignoradas.
            return ResponseEntity.status(HttpStatus.OK).body(cozinhaService.salvar(cozinhaAtual.get()));
        }
        return ResponseEntity.notFound().build();
    } 
    
    /* @DeleteMapping("/{cozinhaId}")
    public ResponseEntity<?> remove(@PathVariable("cozinhaId") Long id){
       try {
           cozinhaService.remove(id);
           return ResponseEntity.noContent().build();
        }catch(EntidadeEmUsoException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
       
        }catch(EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    } */

    @DeleteMapping("/{cozinhaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable("cozinhaId") Long id){
        cozinhaService.remove(id);
    }
}
