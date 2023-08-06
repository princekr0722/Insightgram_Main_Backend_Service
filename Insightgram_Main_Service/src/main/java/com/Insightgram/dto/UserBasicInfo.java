package com.Insightgram.dto;

import com.Insightgram.enties.ProfilePhoto;
import com.Insightgram.enties.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBasicInfo {
	private Integer userId;
	private String username;
	private String userFullName;
	private ProfilePhoto profilePhoto;
	
	public UserBasicInfo(User user) {
		userId = user.getUserId();
		username = user.getUsername();
		userFullName = user.getFirstName()+" "+user.getLastName();
		profilePhoto = user.getProfilePhoto();
	}
}
