
package com.Insightgram.services.impl;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.Insightgram.apiclients.MessagingServiceClient;
import com.Insightgram.config.security.SpringSecurityContants;
import com.Insightgram.dto.MessagingRegistrationUserInfo;
import com.Insightgram.enties.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Service
public class MessagingServiceProvider {

	@Autowired
	MessagingServiceClient messagingClient;
	
	public ResponseEntity<MessagingRegistrationUserInfo> registerUserForMessaging(@NotNull User newUser){
		Integer userId = newUser.getUserId()/1;
		String username = newUser.getUsername().trim();
		
		MessagingRegistrationUserInfo userInfo = new MessagingRegistrationUserInfo(userId, username);
		
		String authString = "" + ":" + SpringSecurityContants.MESSAGING_ACCESS_PASSWORD;
        String authorization = "Basic "+(Base64.getEncoder().encodeToString(authString.getBytes()));
		
		return messagingClient.registerUserForMessaging(userInfo, authorization);
	}
	
	public ResponseEntity<String> getAccessTokenForUserToChat(@NotBlank String username, @NotBlank String sessionJwt){
		String authString = username + ":" + SpringSecurityContants.MESSAGING_ACCESS_PASSWORD;
        String authorization = "Basic "+(Base64.getEncoder().encodeToString(authString.getBytes()));
        
		return messagingClient.getAccessTokenForUserToChat(authorization, sessionJwt);
	}
	
	public ResponseEntity<Void> removeAccessToken(@NotBlank String messagingToken, @NotBlank String sessionJwt){
		return messagingClient.removeAccessToken("Bearer "+messagingToken, sessionJwt);
	}
	
}
