package com.sutanrrier.projeto_spring3.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.sutanrrier.projeto_spring3.dtos.PessoaDto;
import com.sutanrrier.projeto_spring3.entities.Pessoa;
import com.sutanrrier.projeto_spring3.services.PessoaService;

@RestController
@RequestMapping(value = "/pessoa")
public class PessoaController {

	@Autowired
	private PessoaService pessoaService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Pessoa>> listarPessoas() {

		return ResponseEntity.ok().body(pessoaService.listarPessoas());
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarPessoaPorId(@PathVariable Long id) {
		Optional<Pessoa> pessoa = pessoaService.listarPessoaPorId(id);

		if (!pessoa.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe uma pessoa com este id.");
		}

		return ResponseEntity.ok().body(pessoa.get());
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Pessoa> cadastrarPessoa(@RequestBody PessoaDto pessoaDto) {

		Pessoa pessoa = new Pessoa();
		BeanUtils.copyProperties(pessoaDto, pessoa);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.salvarPessoa(pessoa));
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
	public ResponseEntity<String> deletarPessoa(@PathVariable Long id) {
		Optional<Pessoa> pessoa = pessoaService.listarPessoaPorId(id);

		if (!pessoa.isPresent() || pessoa.get().getId() != id) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro! ID inválido ou não existente.");
		}

		pessoaService.apagarPessoa(id);
		return ResponseEntity.ok().body("Pessoa deletada com sucesso!");
	}

}
