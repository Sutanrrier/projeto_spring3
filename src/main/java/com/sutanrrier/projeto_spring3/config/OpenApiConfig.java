package com.sutanrrier.projeto_spring3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenApi() {
		return new OpenAPI().components(new Components())
				.info(new Info()
						.title("Projeto Spring 3 - REST API")
						.version("v1")
						.description("Um projeto com o intuito de revisar conceitos criando uma REST API usando Spring 3"));
	}

}
