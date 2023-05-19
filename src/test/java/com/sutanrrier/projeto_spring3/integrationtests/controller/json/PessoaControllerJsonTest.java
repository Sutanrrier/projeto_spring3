package com.sutanrrier.projeto_spring3.integrationtests.controller.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sutanrrier.projeto_spring3.configs.TestConfigs;
import com.sutanrrier.projeto_spring3.integrationtests.testcontainers.AbstractIntegrationTest;
import com.sutanrrier.projeto_spring3.integrationtests.vo.AccountCredentialsVO;
import com.sutanrrier.projeto_spring3.integrationtests.vo.PessoaVO;
import com.sutanrrier.projeto_spring3.integrationtests.vo.TokenVO;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PessoaControllerJsonTest extends AbstractIntegrationTest{

	private static RequestSpecification requestSpecification;
	private static ObjectMapper objectMapper;
	
	private static PessoaVO pessoa;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		pessoa = new PessoaVO();
	}

	@Test
	@Order(0)
	void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		String acessToken = RestAssured.given()
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(user)
			.when().post().then().statusCode(200).extract().body().as(TokenVO.class).getAcessToken();
		
		requestSpecification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + acessToken)
				.setBasePath("/api/v1/pessoa")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1)
	void testCreate() throws JsonMappingException, JsonProcessingException {
		mockPessoa();
		
		String content = RestAssured.given().spec(requestSpecification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SUTANRRIER)
				.body(pessoa)
			.when().post().then().statusCode(201).extract().body().asString();
		
		PessoaVO pessoaCriada = objectMapper.readValue(content, PessoaVO.class);
		pessoa = pessoaCriada;
		
		//Testes
		assertNotNull(pessoaCriada);
		assertNotNull(pessoaCriada.getNome());
		assertNotNull(pessoaCriada.getSenha());
		
		assertTrue(pessoaCriada.getId() > 0);
		
		assertEquals("Joana Stall", pessoaCriada.getNome());
	}

	private void mockPessoa() {
		pessoa.setNome("Joana Stall");
		pessoa.setSenha("senhaSeguraLALA");
		
	}

}
