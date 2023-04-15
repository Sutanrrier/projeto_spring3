package com.sutanrrier.projeto_spring3.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sutanrrier.projeto_spring3.security.jwt.JwtConfigurer;
import com.sutanrrier.projeto_spring3.security.jwt.JwtTokenProvider;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		
		http.httpBasic().disable().csrf().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
				.requestMatchers("/api/v1/**").authenticated().requestMatchers("/users").denyAll());
	
		http.cors().and().apply(new JwtConfigurer(jwtTokenProvider));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
