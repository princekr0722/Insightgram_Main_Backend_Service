package com.Insightgram.apiclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.Insightgram.dto.MessagingRegistrationUserInfo;

@FeignClient("INSIGHTGRAM-MESSAGING-SERVICE")
public interface MessagingServiceClient {
	
	@PostMapping("/user/chat/register")
	ResponseEntity<MessagingRegistrationUserInfo> registerUserForMessaging(@RequestBody MessagingRegistrationUserInfo userInfo, @RequestHeader("Authorization") String authorization);
	
	@GetMapping("/user/chat/accessToken")
	ResponseEntity<String> getAccessTokenForUserToChat(@RequestHeader("Authorization") String authorizationHeader, @RequestHeader("SessionJWT") String sessionJwt);
	
	@DeleteMapping("/user/chat/remove/accessToken")
	ResponseEntity<Void> removeAccessToken(@RequestHeader("Authorization") String jwtToken, @RequestHeader("SessionJWT") String sessionJwt);
}
