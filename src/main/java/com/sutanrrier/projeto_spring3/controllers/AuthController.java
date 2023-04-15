package com.sutanrrier.projeto_spring3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sutanrrier.projeto_spring3.services.AuthService;
import com.sutanrrier.projeto_spring3.vo.security.AccountCredentialsVO;
import com.sutanrrier.projeto_spring3.vo.security.TokenVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication Endpoint")
@Controller
@RequestMapping(value = "/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Operation(description = "Authenticates a user and returns a token")
	@PostMapping(value = "/signin")
	public ResponseEntity<Object> signin(@RequestBody AccountCredentialsVO data) {
		if (data == null || data.getUsername() == null || data.getPassword() == null || data.getUsername().isBlank()
				|| data.getPassword().isBlank()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Request!");
		}
		
		TokenVO token = authService.signin(data);
		
		if(token == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error! Token is null!");
		}
		
		return ResponseEntity.ok().body(token);
	}
	
	@Operation(description = "Refresh token for authenticated user and returns a token.")
	@PutMapping(value = "/refresh/{username}")
	public ResponseEntity<Object> refreshToken(@PathVariable("username") String username,
			@RequestHeader("Authorization") String refreshToken) {
		if (refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Request!");
		}
		
		TokenVO token = authService.refreshToken(username, refreshToken);
		
		if(token == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error! Token is null!");
		}
		
		return ResponseEntity.ok().body(token);
	}
}
