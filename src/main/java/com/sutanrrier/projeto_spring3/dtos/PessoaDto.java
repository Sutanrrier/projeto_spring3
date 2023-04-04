package com.sutanrrier.projeto_spring3.dtos;

import java.io.Serializable;

public class PessoaDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String nome;
	private String senha;

	// Construtores
	public PessoaDto() {

	}

	public PessoaDto(String nome, String senha) {
		this.nome = nome;
		this.senha = senha;
	}

	// Getters e Setters
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}
