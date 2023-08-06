package com.Insightgram.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.Insightgram.enties.User;
import com.Insightgram.repositories.UserRepository;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String uniqueUserIdentifier = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		Optional<User> opt = userRepository.findByUsername(uniqueUserIdentifier);
		if (opt.isEmpty())
			opt = userRepository.findByMobileNumber(uniqueUserIdentifier);
		
		if (opt.isPresent() && passwordEncoder.matches(password, opt.get().getPassword())) {
			
			User user = opt.get();
			
			Set<GrantedAuthority> authorities = new HashSet<>();
			authorities.add(new SimpleGrantedAuthority(user.getUserRole().toString()));
			
			return new UsernamePasswordAuthenticationToken(uniqueUserIdentifier, password, authorities);
			
		} else {
			throw new BadCredentialsException("Wrong Credentials.");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
