package com.sutanrrier.projeto_spring3.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sutanrrier.projeto_spring3.entities.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	@Query("SELECT p FROM Pessoa p WHERE p.nome LIKE LOWER(CONCAT('%',:name,'%'))")
	Page<Pessoa> findPessoaByNome(@Param("name") String name, Pageable pageable);
}
