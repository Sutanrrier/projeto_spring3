package com.sutanrrier.projeto_spring3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sutanrrier.projeto_spring3.entities.User;
import com.sutanrrier.projeto_spring3.repositories.UserRepository;
import com.sutanrrier.projeto_spring3.security.jwt.JwtTokenProvider;
import com.sutanrrier.projeto_spring3.vo.security.AccountCredentialsVO;
import com.sutanrrier.projeto_spring3.vo.security.TokenVO;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserRepository repository;

	public TokenVO signin(AccountCredentialsVO data) {
		try {
			String username = data.getUsername();
			String password = data.getPassword();
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			User user = repository.findByUsername(username);
			TokenVO tokenResponse = new TokenVO();

			if (user != null) {
				tokenResponse = jwtTokenProvider.createAcessToken(username, user.getRoles());
			} else {
				throw new UsernameNotFoundException("Username not found!");
			}

			return tokenResponse;
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid username/password!");
		}
	}

	public TokenVO refreshToken(String username, String refreshToken) {
		User user = repository.findByUsername(username);
		TokenVO tokenResponse = new TokenVO();

		if (user != null) {
			tokenResponse = jwtTokenProvider.refreshToken(refreshToken);
		} else {
			throw new UsernameNotFoundException("Username not found!");
		}

		return tokenResponse;
	}

}
