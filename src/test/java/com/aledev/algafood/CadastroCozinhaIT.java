package com.aledev.algafood;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CadastroCozinhaIT {
	//teste sao divididos em cenario -> acao -> validacao
	@LocalServerPort
	private Integer port;
	@BeforeEach
	public void setUp() {
		enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/cozinhas";
	}
	@Test
	public void deveRetornarStatus200_quandoConsultarCozinha(){
	enableLoggingOfRequestAndResponseIfValidationFails();
	given().accept(ContentType.JSON).when().get()
				.then().statusCode(HttpStatus.OK.value());
	}
	@Test
	public void deveConter4Cozinhas_quandoConsultarCozinha(){
		enableLoggingOfRequestAndResponseIfValidationFails();
		given().accept(ContentType.JSON).when().get()
				.then().body("", hasSize(4)).body("nome", hasItems("Indiana", "Tailandesa"));
	}
	@Test
	public void deveRetornarStatus201_QuandoCadastrarCozinha() {
		given().body("{ \"nome\": \"Chinesa\" }").contentType(ContentType.JSON).accept(ContentType.JSON)
				.when().post().then().statusCode(HttpStatus.CREATED.value());
	}
}
