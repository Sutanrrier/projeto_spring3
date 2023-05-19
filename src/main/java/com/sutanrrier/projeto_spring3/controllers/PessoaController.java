package com.sutanrrier.projeto_spring3.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sutanrrier.projeto_spring3.dtos.PessoaDto;
import com.sutanrrier.projeto_spring3.entities.Pessoa;
import com.sutanrrier.projeto_spring3.services.PessoaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "api/v1/pessoa")
@Tag(name = "Pessoa", description = "Endpoints para administrar pessoas.")
public class PessoaController {

	@Autowired
	private PessoaService pessoaService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
			description = "Retorna todas as pessoas do banco.", 
			tags = {"Pessoa"}, 
			responses = { 
					@ApiResponse(description = "Sucess", responseCode = "200", 
							content = {
									@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PessoaDto.class))) 
							}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),

	})
	public ResponseEntity<List<Pessoa>> listarPessoas() {
		List<Pessoa> listaPessoas = pessoaService.listarPessoas();

		// Exemplo de implementação de HATEOAS
		listaPessoas.stream()
				.forEach(p -> p.add(WebMvcLinkBuilder
						.linkTo(WebMvcLinkBuilder.methodOn(PessoaController.class).listarPessoaPorId(p.getId()))
						.withSelfRel()));

		return ResponseEntity.ok().body(listaPessoas);
	}
	
	@GetMapping(value = "/p")
	public ResponseEntity<Page<Pessoa>> listarPessoasPaginado(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size) {
	
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.ASC, "id"));
		
		Page<Pessoa> listaPessoasPaginado = pessoaService.listarPessoasPaginado(pageable);

		return ResponseEntity.ok().body(listaPessoasPaginado);
	}
	
	@GetMapping(value = "/listarPorNomePaginado")
	public ResponseEntity<Page<Pessoa>> listarPessoasPorNomePaginado(
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size,
			@RequestParam(defaultValue = "") String name) {
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.ASC, "id"));
		
		Page<Pessoa> listaPessoasPaginado = pessoaService.listarPessoasPorNomePaginado(name, pageable);
		
		return ResponseEntity.ok().body(listaPessoasPaginado);
	}


	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
			description = "Retorna uma pessoa no banco a partir de um 'id'.", 
			tags = {"Pessoa"}, 
			responses = { 
					@ApiResponse(description = "Sucess", responseCode = "200", 
							content = {
									@Content(schema = @Schema(implementation = PessoaDto.class)) 
							}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),

	})
	public ResponseEntity<Object> listarPessoaPorId(@PathVariable Long id) {
		Optional<Pessoa> pessoa = pessoaService.listarPessoaPorId(id);

		if (!pessoa.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe uma pessoa com este id.");
		}

		// Exemplo de implementação de HATEOAS
		pessoa.get().add(WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(PessoaController.class).listarPessoaPorId(id)).withSelfRel());

		return ResponseEntity.ok().body(pessoa.get());
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
			description = "Cria uma pessoa nova no banco a partir de um objeto JSON enviado no corpo da requisição.", 
			tags = {"Pessoa"}, 
			responses = { 
					@ApiResponse(description = "Created", responseCode = "201", 
							content = {
									@Content(schema = @Schema(implementation = PessoaDto.class)) 
							}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),

	})
	public ResponseEntity<Pessoa> cadastrarPessoa(@RequestBody PessoaDto pessoaDto) {

		Pessoa pessoa = new Pessoa();
		BeanUtils.copyProperties(pessoaDto, pessoa);

		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.salvarPessoa(pessoa));
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
			description = "Atualiza uma pessoa no banco a partir de um objeto JSON enviado no corpo da requisição.", 
			tags = {"Pessoa"}, 
			responses = { 
					@ApiResponse(description = "Created", responseCode = "201", 
							content = {
									@Content(schema = @Schema(implementation = PessoaDto.class)) 
							}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),

	})
	public ResponseEntity<Object> atualizarPessoa(@RequestBody PessoaDto pessoaDtoAtualizada, @PathVariable Long id) {
		Optional<Pessoa> pessoa = pessoaService.listarPessoaPorId(id);

		if (!pessoa.isPresent() || pessoa.get().getId() != id) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! ID inválido ou não existente.");
		}

		Pessoa pessoaAtualizada = new Pessoa();
		BeanUtils.copyProperties(pessoaDtoAtualizada, pessoaAtualizada);
		pessoaAtualizada.setId(pessoa.get().getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.salvarPessoa(pessoaAtualizada));
	}

	@DeleteMapping(value = "/{id}")
	@Operation(
			description = "Apaga uma pessoa no banco a partir de um 'id'.", 
			tags = {"Pessoa"}, 
			responses = { 
					@ApiResponse(description = "Sucess", responseCode = "200", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),

	})
	public ResponseEntity<String> deletarPessoa(@PathVariable Long id) {
		Optional<Pessoa> pessoa = pessoaService.listarPessoaPorId(id);

		if (!pessoa.isPresent() || pessoa.get().getId() != id) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! ID inválido ou não existente.");
		}

		pessoaService.apagarPessoa(id);
		return ResponseEntity.ok().body("Pessoa deletada com sucesso!");
	}

}
