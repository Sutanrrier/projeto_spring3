package com.sutanrrier.projeto_spring3.integrationtests.vo;

import java.io.Serializable;

public class PessoaVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String senha;

	// Construtores
	public PessoaVO() {

	}

	public PessoaVO(String nome, String senha) {
		this.setId(null);
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
