package com.aledev.algafood;

import com.aledev.algafood.domain.exceptions.CozinhaNaoEncontradaException;
import com.aledev.algafood.domain.exceptions.EntidadeEmUsoException;
import com.aledev.algafood.domain.model.Cozinha;
import com.aledev.algafood.domain.service.CadastroCozinhaService;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CadastroCozinhaIT {
	//teste sao divididos em cenario -> acao -> validacao

	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	@Test
	public void deveAtribuirId_QuandoCadastrarCozinhaComDadosCorretos() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("chinesa");
		novaCozinha = cadastroCozinha.salvar(novaCozinha);

		Assertions.assertThat(novaCozinha).isNotNull();
		Assertions.assertThat(novaCozinha.getId()).isNotNull();
	}

	@Test
	public void deveFalhar_QuandoCadastrarCozinhaSemNome() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);

		ConstraintViolationException erroEsperado =
			org.junit.jupiter.api.Assertions.assertThrows(ConstraintViolationException.class, () -> {
				cadastroCozinha.salvar(novaCozinha);
			});
		Assertions.assertThat(erroEsperado).isNotNull();
	}
	@Test
	public void deveFalhar_QuandoExcluirCozinhaEmUso() {
		EntidadeEmUsoException erroEsperado =
				org.junit.jupiter.api.Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
					cadastroCozinha.remove(1L);
				});
		Assertions.assertThat(erroEsperado).isNotNull();
	}

	@Test
	public void deveFalhar_QuandoExcluirCozinhaInexistente() {
		CozinhaNaoEncontradaException erroEsperado =
				org.junit.jupiter.api.Assertions.assertThrows(CozinhaNaoEncontradaException.class, () -> {
					cadastroCozinha.remove(90L);
				});
		Assertions.assertThat(erroEsperado).isNotNull();
	}
}
