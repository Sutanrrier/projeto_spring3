package com.sutanrrier.projeto_spring3.integrationtests.swagger;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.sutanrrier.projeto_spring3.configs.TestConfigs;
import com.sutanrrier.projeto_spring3.integrationtests.testcontainers.AbstractIntegrationTest;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest{

	@Test
	void shouldDisplaySwaggerUiPage() {
		String content = RestAssured.given().basePath("/swagger-ui/index.html").port(TestConfigs.SERVER_PORT)
			.when().get().then().statusCode(200).extract().body().asString();
		
		assertTrue(content.contains("Swagger UI"));
	}

}
