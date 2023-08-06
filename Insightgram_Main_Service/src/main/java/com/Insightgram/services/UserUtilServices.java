package com.Insightgram.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Insightgram.enties.User;
import com.Insightgram.enties.enums.AccountType;
import com.Insightgram.exceptions.UserException;
import com.Insightgram.repositories.UserRepository;

@Service
public class UserUtilServices {

	@Autowired
	private UserRepository userRepository;
	
	public User getCurrentUser() {
		String uniqueUserTdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Optional<User> opt = userRepository.findByUsername(uniqueUserTdentifier);
		if(opt.isEmpty()) opt = userRepository.findByMobileNumber(uniqueUserTdentifier);
		
		if(opt.isEmpty()) throw new UserException("Not authorized to make a post.");
		
		User user = opt.get();
		return user;
	}
	
	public boolean canInteractWithAnotherUser(Integer currentUserId, User userToInteract) {
		boolean check1 = false;
		if(userToInteract.getAccountType() == AccountType.PRIVATE) {
			if(currentUserId == userToInteract.getUserId()) {
				check1 = true;
			}
			else {
				for(User follower: userToInteract.getFollowers()) {
					if(follower.getUserId() == currentUserId) {
						check1 = true;
						break;
					}
				}
			}
		}else check1 = true;
		return check1;
	}
	
	public int getUserIdByUsernameOrMobile(String usernameOrMobile) {
		Optional<Integer> opt = userRepository.getUserIdByUsernameOrMobile(usernameOrMobile);
		if(opt.isEmpty()) throw new UserException("Invalid user");
		else return opt.get();
	}
	
	public User getUserById(Integer userId) {
		Optional<User> opt = userRepository.findById(userId);
		if(opt.isEmpty()) throw new UserException("No user found with Id: "+userId);
		else return opt.get(); 
	}
}
