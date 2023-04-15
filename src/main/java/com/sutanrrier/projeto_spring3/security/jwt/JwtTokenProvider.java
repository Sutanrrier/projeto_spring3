package com.sutanrrier.projeto_spring3.security.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.sutanrrier.projeto_spring3.vo.security.TokenVO;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key}")
	private String secretKey;

	@Value("${security.jwt.token.expire-length}")
	private long expireLenght;

	@Autowired
	private UserDetailsService userDetailsService;

	Algorithm algorithm = null;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		algorithm = Algorithm.HMAC256(secretKey.getBytes());
	}

	public TokenVO createAcessToken(String username, List<String> roles) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + expireLenght);

		String acessToken = getAcessToken(username, roles, now, validity);
		String refreshToken = getRefreshToken(username, roles, now);

		return new TokenVO(username, true, now, validity, acessToken, refreshToken);
	}

	public TokenVO refreshToken(String refreshToken) {
		if(refreshToken.contains("Bearer ")) {
			refreshToken = refreshToken.substring(7);
		}
		
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = verifier.verify(refreshToken);
		
		String username = decodedJWT.getSubject();
		List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
		
		return createAcessToken(username, roles);
	}
	
	private String getRefreshToken(String username, List<String> roles, Date now) {
		Date validityRefreshToken = new Date(now.getTime() + (expireLenght * 3));

		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(validityRefreshToken)
				.withSubject(username)
				.sign(algorithm)
				.strip();
	}

	private String getAcessToken(String username, List<String> roles, Date now, Date validity) {
		String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		
		return JWT.create()
				.withClaim("roles", roles)
				.withIssuedAt(now)
				.withExpiresAt(validity)
				.withSubject(username)
				.withIssuer(issuerUrl)
				.sign(algorithm)
				.strip();
	}

	public Authentication getAuthentication(String token) {
		DecodedJWT decodedJWT = decodedToken(token);

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());

		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private DecodedJWT decodedToken(String token) {
		Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
		JWTVerifier verifier = JWT.require(alg).build();
		DecodedJWT decodedJWT = verifier.verify(token);
		
		return decodedJWT;
	}

	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		
		if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		
		return null;
	}
	
	public boolean validateToken(String token) {
		DecodedJWT decodedJWT = decodedToken(token);

		return (decodedJWT.getExpiresAt().before(new Date())) ? false : true;
	}
	
}
