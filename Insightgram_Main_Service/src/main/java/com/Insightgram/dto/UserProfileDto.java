package com.Insightgram.dto;

import java.time.LocalDate;

import com.Insightgram.enties.User;
import com.Insightgram.enties.enums.Gender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto extends UserDto{
	
	private int noOfNotification;
	private Gender gender;
	private LocalDate dob;
	
	public UserProfileDto(User user) {
		super(user);
		noOfNotification = user.getNotifications().size();
		gender = user.getGender();
		dob = user.getDob();
	}
}
