package com.sutanrrier.projeto_spring3.unittests.mockito.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sutanrrier.projeto_spring3.entities.Pessoa;
import com.sutanrrier.projeto_spring3.mocks.MockPessoa;
import com.sutanrrier.projeto_spring3.repositories.PessoaRepository;
import com.sutanrrier.projeto_spring3.services.PessoaService;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PessoaControllerTest {

	MockPessoa input;
	
	@InjectMocks
	private PessoaService pessoaService;
	
	@Mock
	PessoaRepository pessoaRepository;
	
	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockPessoa();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testListarPessoas() {
		List<Pessoa> listaPessoas = input.mockEntityList();
		
		Mockito.when(pessoaRepository.findAll()).thenReturn(listaPessoas);
		
		List<Pessoa> resultado = pessoaRepository.findAll();
		
		assertNotNull(resultado);
		assertEquals(10, resultado.size());
		
		Pessoa pessoa8 = resultado.get(8);
		assertEquals("Pessoa 8", pessoa8.getNome());
		
		Pessoa pessoa9 = resultado.get(9);
		assertEquals("Senha 9", pessoa9.getSenha());
	}

	@Test
	void testListarPessoaPorId() {
		Pessoa pessoa = input.mockEntity(1);
		pessoa.setId(1L);
		
		Mockito.when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
		
		Pessoa resultado = pessoaService.listarPessoaPorId(1L).get();
		assertNotNull(resultado);
		assertNotNull(resultado.getId());
		assertNotNull(resultado.getNome());
		assertEquals("Senha 1", resultado.getSenha());
	
	}

	@Test
	void testCadastrarPessoa() {
		Pessoa pessoa = input.mockEntity(2);
		Pessoa pessoaCriada = pessoa;
		pessoaCriada.setId(1L);

		Mockito.when(pessoaRepository.save(pessoa)).thenReturn(pessoaCriada);
		
		Pessoa resultado = pessoaService.salvarPessoa(pessoa);
		assertNotNull(resultado);
		assertNotNull(resultado.getId());
		assertNotNull(resultado.getNome());
		assertEquals("Senha 2", resultado.getSenha());
	}

	@Test
	void testAtualizarPessoa() {
		Pessoa pessoa = input.mockEntity(2);
		pessoa.setId(2L);
		
		Pessoa pessoaAtualizada = pessoa;
		pessoaAtualizada.setId(3L);
		pessoaAtualizada.setNome("Sérgio");
		
		Mockito.when(pessoaRepository.save(pessoa)).thenReturn(pessoaAtualizada);
		
		Pessoa resultado = pessoaService.salvarPessoa(pessoa);
		assertNotNull(resultado);
		assertNotNull(resultado.getId());
		assertNotNull(resultado.getNome());
		assertEquals(3L, resultado.getId());
		assertEquals("Sérgio", resultado.getNome());
	}

	@Test
	void testDeletarPessoa() {
		Pessoa pessoa = input.mockEntity(4);
		pessoa.setId(4L);
		
		pessoaService.apagarPessoa(4L);
	}

}
