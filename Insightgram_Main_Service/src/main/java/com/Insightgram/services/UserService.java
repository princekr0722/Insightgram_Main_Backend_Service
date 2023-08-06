package com.Insightgram.services;

import com.Insightgram.dto.UserBasicInfo;
import com.Insightgram.dto.UserDto;
import com.Insightgram.dto.UserProfileDto;
import com.Insightgram.enties.User;
import com.Insightgram.entities.forms.EditProfileForm;
import com.Insightgram.entities.forms.PersonalInfoForm;
import com.Insightgram.entities.forms.UserCreationForm;
import com.Insightgram.models.PageOf;

public interface UserService {
	
	User createNewUser(UserCreationForm userCreationForm);
	User getUser(String uniqueUserIdentifier);
	User getUser(String uniqueUserIdentifier, String errorMessage);
	UserDto getPublicProfile(Integer userId);
	UserProfileDto getMyProfile();
	User deleteUser(String password) throws IllegalAccessException;
	
	public PageOf<UserBasicInfo> searchUserPageWise(String keyword, Integer pageSize, Integer pageNumber, SortUserBy sortBy,
			SortType sortType);
	public PageOf<UserBasicInfo> searchUserPageWise(String keyword, Integer pageSize, Integer pageNumber, SortUserBy sortBy);
	public PageOf<UserBasicInfo> searchUserPageWise(String keyword, Integer pageSize, Integer pageNumber);

	boolean isUsernameOccupied(String username);
	boolean isMobileNumberRegistered(String mobileNumber);
	
	String changeAccountVisiblity();
	
	String changeBasicUserInfo(PersonalInfoForm basicUserInfoForm);
	
	String editProfile(EditProfileForm editProfileForm);
	
	UserBasicInfo getUserBasicInfoByUsername(String username);
}
