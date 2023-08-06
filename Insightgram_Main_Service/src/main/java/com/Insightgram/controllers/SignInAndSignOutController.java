package com.Insightgram.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Insightgram.config.security.SpringSecurityContants;
import com.Insightgram.dto.UserProfileDto;
import com.Insightgram.enties.User;
import com.Insightgram.entities.forms.UserCreationForm;
import com.Insightgram.services.TokenService;
import com.Insightgram.services.UserService;
import com.Insightgram.services.impl.MessagingServiceProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@Transactional
@RequestMapping("/main-app")
public class SignInAndSignOutController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MessagingServiceProvider messagingServiceProvider;
	
	@PostMapping("/signUp")
	public String userCreation(@Valid @RequestBody UserCreationForm userCreationForm) {
		userCreationForm.setPassword(passwordEncoder.encode(userCreationForm.getPassword()));
	
		User newUser = userService.createNewUser(userCreationForm);
		messagingServiceProvider.registerUserForMessaging(newUser);
	
		return "Account creation successfull";
	}
	
	@GetMapping("/signIn")
	public ResponseEntity<UserProfileDto> logInUser(Authentication authentication, HttpSession httpSession){
		String uniqueUserIdentifier = authentication.getName();
		
		String errorMessage = "Wrong Credentials";
		UserProfileDto userProfile = new UserProfileDto(userService.getUser(uniqueUserIdentifier, errorMessage));
		
		return new ResponseEntity<UserProfileDto>(userProfile, HttpStatus.OK);
	}
	
	@GetMapping("/signOut")
	@Transactional
	public ResponseEntity<String> logout(HttpServletRequest request) {
		SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        
        String jwt = request.getHeader(SpringSecurityContants.JWT_HEADER).substring(7);
        String messagingToken = request.getHeader(SpringSecurityContants.MESSAGING_JWT_HEADER);
        

        String message = "Logout Successful";
        
//        Messaging Service LogOut
        messagingServiceProvider.removeAccessToken(messagingToken, jwt);
        
//        Main Service LogOut
        if(!tokenService.revokeToken(jwt)){
        	message = "Internal server error.";
			return new ResponseEntity<String>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
        	return new ResponseEntity<String>(message, HttpStatus.OK);
        }
    }
	
	@GetMapping("/username/check/{username}")
	public ResponseEntity<Boolean> isUsernameAvailable(@PathVariable String username) {
		return new ResponseEntity<Boolean>(!userService.isUsernameOccupied(username), HttpStatus.OK);
	}
	
	@GetMapping("/mobileNumber/check/{mobileNumber}")
	public ResponseEntity<Boolean> isMobileNumberAvailable(@PathVariable String mobileNumber) {
		return new ResponseEntity<Boolean>(!userService.isMobileNumberRegistered(mobileNumber), HttpStatus.OK);
	}
	
	@GetMapping("/token/check")
	public ResponseEntity<Boolean> isTokenValid(@RequestHeader String token) {
		return new ResponseEntity<Boolean>(tokenService.isTokenValid(token), HttpStatus.OK);
	}
}
