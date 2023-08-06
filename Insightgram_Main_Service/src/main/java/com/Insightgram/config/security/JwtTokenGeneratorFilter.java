package com.Insightgram.config.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Insightgram.enties.Token;
import com.Insightgram.enties.User;
import com.Insightgram.enties.enums.TokenType;
import com.Insightgram.repositories.TokenRepository;
import com.Insightgram.services.TokenService;
import com.Insightgram.services.UserUtilServices;
import com.Insightgram.services.impl.MessagingServiceProvider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenGeneratorFilter extends OncePerRequestFilter{

	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserUtilServices userUtilServices;
	
	@Autowired
	private MessagingServiceProvider messagingServiceProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication!=null) {
			
			SecretKey key = Keys.hmacShaKeyFor(SpringSecurityContants.JWT_KEY.getBytes());
			
			String jwt = Jwts.builder()
						.setIssuer("Prince Kumar")
						.setSubject("Insightgram JWT Token")
						.claim("uniqueUserIdentifier", authentication.getName())
						.claim("authorities", populateAuthorities(authentication.getAuthorities()))
						.setIssuedAt(new Date())
//						.setExpiration(new Date( new Date().getTime() + 30000000))
						.signWith(key).compact();
			
			User user = userUtilServices.getCurrentUser();
			Token token = Token.builder()
						.token(jwt)
						.tokenType(TokenType.BEARER)
						.expired(false)
						.revoked(false)
						.user(user)
						.build();
			
			Token newToken = tokenRepository.save(token);
			
//			Messaging Access Token Generation
			String messagingToken = messagingServiceProvider
						.getAccessTokenForUserToChat(
								user.getUsername(), jwt
						).getBody();
			
//			tokenService.scheduleRevokeToken(newToken.getTokenId(), 30000000, TimeUnit.MILLISECONDS);
			
			response.addHeader(SpringSecurityContants.JWT_HEADER, jwt);
			response.addHeader(SpringSecurityContants.MESSAGING_JWT_HEADER, messagingToken);
			
		}
		
		filterChain.doFilter(request, response);
		
	}

	private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<String> authoritiesSet = new HashSet<>();
		for(GrantedAuthority ga: authorities) {
			authoritiesSet.add(ga.getAuthority());
		}
		return String.join(",", authoritiesSet);
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !request.getServletPath().equals("/main-app/signIn");
	}
}
