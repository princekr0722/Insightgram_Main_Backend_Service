package com.Insightgram.dto;

import com.Insightgram.enties.ProfilePhoto;
import com.Insightgram.enties.User;
import com.Insightgram.enties.enums.AccountType;
import com.Insightgram.enties.enums.UserRole;

import lombok.Data;

@Data
public class UserDto {
	
	private int userId;
	private String username;
	private ProfilePhoto profilePhoto;
	private UserRole userType;
	private AccountType accountType;
	private String firstName;
	private String lastName;
	private String bio;
	private String website;
	private int noOfStories;
	private int noOfFollowers;
	private int noOfFollowing;
	private int noOfPosts;
	
	public UserDto (User user) {
		userId = user.getUserId();
		this.profilePhoto = user.getProfilePhoto();
		username = user.getUsername();
		userType = user.getUserRole();
		accountType = user.getAccountType();
		firstName = user.getFirstName();
		lastName = user.getLastName();
		bio = user.getBio();
		website = user.getWebsite();
		noOfStories = user.getStories().size();
		noOfFollowers = user.getFollowers().size();
		noOfFollowing = user.getFollowing().size();
		noOfPosts = user.getPosts().size();
	}
}
