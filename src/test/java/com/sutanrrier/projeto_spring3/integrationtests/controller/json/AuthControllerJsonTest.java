package com.sutanrrier.projeto_spring3.integrationtests.controller.json;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sutanrrier.projeto_spring3.configs.TestConfigs;
import com.sutanrrier.projeto_spring3.integrationtests.testcontainers.AbstractIntegrationTest;
import com.sutanrrier.projeto_spring3.integrationtests.vo.AccountCredentialsVO;
import com.sutanrrier.projeto_spring3.integrationtests.vo.TokenVO;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest {

	private static TokenVO tokenVO;
	
	@Test
	@Order(1)
	void testSignin() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		tokenVO = RestAssured.given()
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(user)
			.when().post().then().statusCode(200).extract().body().as(TokenVO.class);
		
		assertNotNull(tokenVO.getUsername());
		assertNotNull(tokenVO.getAcessToken());
		assertNotNull(tokenVO.getRefreshToken());
		
	}
	
	@Test
	@Order(2)
	void testRefresh() throws JsonMappingException, JsonProcessingException {
		TokenVO newTokenVO = RestAssured.given()
				.basePath("/auth/refresh")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("username", tokenVO.getUsername())
				.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
			.when().put("{username}").then().statusCode(200).extract().body().as(TokenVO.class);
		
		assertNotNull(newTokenVO.getAcessToken());
		assertNotNull(newTokenVO.getRefreshToken());
		
	}
}
