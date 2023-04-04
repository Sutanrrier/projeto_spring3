package com.sutanrrier.projeto_spring3.mocks;

import java.util.ArrayList;
import java.util.List;

import com.sutanrrier.projeto_spring3.entities.Pessoa;

public class MockPessoa {

	public Pessoa mockEntity(Integer numero) {
		Pessoa pessoa = new Pessoa();
		
		pessoa.setNome("Pessoa " + numero);
		pessoa.setSenha("Senha " + numero);
		
		return pessoa;
	}
	
	public List<Pessoa> mockEntityList(){
		List<Pessoa> listaPessoas = new ArrayList<>();
		
		for(int i = 0; i < 10; i++) {
			listaPessoas.add(mockEntity(i));
			
		}
		
		return listaPessoas;
	}
	
}
