package com.sutanrrier.projeto_spring3.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sutanrrier.projeto_spring3.entities.Pessoa;
import com.sutanrrier.projeto_spring3.repositories.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	public List<Pessoa> listarPessoas() {
		return pessoaRepository.findAll();
	}

	public Optional<Pessoa> listarPessoaPorId(Long id) {
		return pessoaRepository.findById(id);
	}

	public Pessoa salvarPessoa(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}

	public void apagarPessoa(Long id) {
		pessoaRepository.deleteById(id);
	}

	public Page<Pessoa> listarPessoasPaginado(Pageable pageable) {
		return pessoaRepository.findAll(pageable);
	}
	
	public Page<Pessoa> listarPessoasPorNomePaginado(String name, Pageable pageable) {
		return pessoaRepository.findPessoaByNome(name, pageable);
	}
}
