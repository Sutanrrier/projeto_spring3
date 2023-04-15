package com.sutanrrier.projeto_spring3.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sutanrrier.projeto_spring3.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	@Query("SELECT u from User u WHERE u.userName =:userName")
	User findByUsername(@Param("userName") String userName);
}
