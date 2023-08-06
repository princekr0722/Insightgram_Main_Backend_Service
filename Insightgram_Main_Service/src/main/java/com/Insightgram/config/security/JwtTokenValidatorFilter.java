package com.Insightgram.config.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Insightgram.enties.Token;
import com.Insightgram.repositories.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenValidatorFilter extends OncePerRequestFilter{

	@Autowired
	private TokenRepository tokenRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String jwt = request.getHeader(SpringSecurityContants.JWT_HEADER);
		if(jwt!=null) {
			
			try {
				jwt = jwt.substring(7);
				
				isValidToken(jwt);
				
				SecretKey key = Keys.hmacShaKeyFor(SpringSecurityContants.JWT_KEY.getBytes());
				
				Claims claims = Jwts.parserBuilder()
								.setSigningKey(key).build()
								.parseClaimsJws(jwt).getBody();
				
				String uniqueUserIdentifier = (String) claims.get("uniqueUserIdentifier");
				String authoritiesString = (String) claims.get("authorities");
				
				List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesString);
				
				Authentication authentication = new UsernamePasswordAuthenticationToken(uniqueUserIdentifier, null, authorities);
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
//				throw new BadCredentialsException("Invalid JWT Token.");
				System.out.println("Invalid token received.");
			}
			
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	private void isValidToken(String jwt) {
		Optional<Token> tokenOpt = tokenRepository.findByToken(jwt);
		Token token = tokenOpt.get();
		if(token.getExpired() || token.getRevoked()) {
			throw new BadCredentialsException("Jwt token is not valid anymore");
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getServletPath().equals("/main-app/signIn");
	}
}
