package com.Insightgram.config.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfiguration {

	@Autowired
	private JwtTokenGeneratorFilter jwtTokenGeneratorFilter;
	@Autowired
	private JwtTokenValidatorFilter jwtTokenValidatorFilter;
	
	private static final String[] AUTH_WHITE_LIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
    };
	
	@Bean
	SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
		
		httpSecurity
		.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.POST, "/main-app/post").hasAnyRole("CUSTOMER", "ADMIN")
				.requestMatchers(HttpMethod.POST, "/main-app/signUp").permitAll()
				.requestMatchers(HttpMethod.GET, "/main-app/mobileNumber/check/**"
						, "/main-app/username/check/**", "/main-app/token/check").permitAll()
				.requestMatchers(AUTH_WHITE_LIST).permitAll()
				.anyRequest().authenticated()
		)
		.addFilterBefore(jwtTokenValidatorFilter, BasicAuthenticationFilter.class)
		.addFilterAfter(jwtTokenGeneratorFilter, BasicAuthenticationFilter.class)
		.csrf(csrf -> csrf.disable())
		.cors(Customizer.withDefaults())
		.formLogin(Customizer.withDefaults())
		.httpBasic(Customizer.withDefaults());
		
		return httpSecurity.build();
		
	}
	
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setExposedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(false);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source; 
	}
}
