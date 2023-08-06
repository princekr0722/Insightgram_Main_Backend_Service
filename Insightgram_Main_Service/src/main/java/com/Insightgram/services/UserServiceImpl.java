package com.Insightgram.services;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Insightgram.dto.UserBasicInfo;
import com.Insightgram.dto.UserDto;
import com.Insightgram.dto.UserProfileDto;
import com.Insightgram.enties.Post;
import com.Insightgram.enties.User;
import com.Insightgram.enties.enums.AccountType;
import com.Insightgram.enties.enums.Gender;
import com.Insightgram.entities.forms.EditProfileForm;
import com.Insightgram.entities.forms.PersonalInfoForm;
import com.Insightgram.entities.forms.UserCreationForm;
import com.Insightgram.exceptions.UserException;
import com.Insightgram.models.PageOf;
import com.Insightgram.repositories.UserRepository;
import com.Insightgram.services.impl.MessagingServiceProvider;
import com.Insightgram.utils.Urls;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserUtilServices userUtilServices;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PostService postService;
	
	@Autowired
	private MessagingServiceProvider  messagingServiceProvider;

	@Override
	@Transactional
	public User createNewUser(@Valid UserCreationForm userCreationForm) {
		User user = new User(userCreationForm);
		
		//User account for Main App
		User newUser = userRepository.save(user);
		
		return newUser;
	}
	
	@Override
	public User getUser(String uniqueUserIdentifier, String errorMessage) {
		Optional<User> opt = userRepository.findByMobileNumber(uniqueUserIdentifier);
		if (opt.isEmpty())
			opt = userRepository.findByUsername(uniqueUserIdentifier);
		if (opt.isEmpty())
			throw new IllegalArgumentException(errorMessage);
		else {
			User user = opt.get();
			return user;
		}
	}
	
	@Override
	public User getUser(String uniqueUserIdentifier) {
		String errorMessage = "No user found with identifier: " + uniqueUserIdentifier;
		return getUser(uniqueUserIdentifier, errorMessage);
	}

	@Override
	public UserDto getPublicProfile(Integer userId) {
		UserDto userPublicProfile = userRepository.getPublicProfileOfUser(userId)
				.orElseThrow(()-> new UserException("No user found with Id: "+userId));
		return userPublicProfile;
	}
	
	@Override
	public UserProfileDto getMyProfile() {
		String currentUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
		UserProfileDto userProfile = userRepository.getMyProfile(currentUserIdentifier)
				.orElseThrow(()-> new UserException("Unexpected error occured, current user was meant to found but could't found."));
		return userProfile;
	}
	
	@Override
	@Transactional
	public User deleteUser(String password) throws IllegalAccessException {
		User user = userUtilServices.getCurrentUser();

		if (passwordEncoder.matches(password, user.getPassword())) {

			for (Post post : user.getPosts()) {
				postService.deletePostById(post.getPostId());
			}

			userRepository.delete(user);

		} else {
			throw new BadCredentialsException("Wrong Password.");
		}

		return user;
	}

	@Override
	public PageOf<UserBasicInfo> searchUserPageWise(String keyword, Integer pageSize, Integer pageNumber, SortUserBy sortBy,
			SortType sortType) {
		if(pageNumber<0) throw new IllegalArgumentException("Page index must not be less than or equals to zero");
		Sort sort;
		if(sortType==SortType.ASC) sort = Sort.by(sortBy.toString()).ascending();
		else sort = Sort.by(sortBy.toString()).descending();
				
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<UserBasicInfo> searchResult = userRepository.searchUserPageWise(keyword, pageable);
		return new PageOf<>(searchResult);
	}
	
	@Override
	public PageOf<UserBasicInfo> searchUserPageWise(String keyword, Integer pageSize, Integer pageNumber,
			SortUserBy sortBy) {
		if (pageNumber < 0)
			throw new IllegalArgumentException("Page index must not be less than or equals to zero");

		return searchUserPageWise(keyword, pageSize, pageNumber, sortBy, SortType.ASC);
	}
	
	@Override
	public PageOf<UserBasicInfo> searchUserPageWise(String keyword, Integer pageSize, Integer pageNumber) {
		if (pageNumber < 0)
			throw new IllegalArgumentException("Page index must not be less than or equals to zero");

		return searchUserPageWise(keyword, pageSize, pageNumber, SortUserBy.userId, SortType.ASC);
	}
	
	@Override
	public boolean isUsernameOccupied(String username) {
		return userRepository.existsByUsername(username);
	}
	
	@Override
	public boolean isMobileNumberRegistered(String mobileNumber) {
		return userRepository.existsByMobileNumber(mobileNumber);
	}

	@Override
	public String changeAccountVisiblity() {
		String currentUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
		AccountType accountType = userRepository.getAccountTypeOfUser(currentUserIdentifier);
		
		AccountType newAccountType;
		if(accountType == AccountType.PRIVATE) {
			userRepository.changeAccountTypeOfUser(currentUserIdentifier, AccountType.PUBLIC);
			newAccountType = AccountType.PUBLIC;
		}else {
			userRepository.changeAccountTypeOfUser(currentUserIdentifier, AccountType.PRIVATE);
			newAccountType = AccountType.PRIVATE;
		}
		return "Account is now "+newAccountType;
	}
	
	@Override
	public String changeBasicUserInfo(PersonalInfoForm basicUserInfoForm) {
		String currentUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
		
		String firstName = basicUserInfoForm.getFirstName();
		String lastName = basicUserInfoForm.getLastName();
		LocalDate dob = basicUserInfoForm.getDob();
		
		userRepository.changeBasicUserInfo(firstName, lastName, dob, currentUserIdentifier);
		return "Changes are applied";
	}
	
	@Override
	public String editProfile(EditProfileForm editProfileForm) {
		String currentUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
		
		String bio = editProfileForm.getBio().trim();
		String website = editProfileForm.getWebsite().trim();
		Gender gender = editProfileForm.getGender();
		
		if(website!=null && !website.equals("")) {
			if(!Urls.isUrlValid(editProfileForm.getWebsite()))
				throw new IllegalArgumentException("Website's URL is not valid.");
		}
		
		userRepository.editUserProfile(bio, website, gender, currentUserIdentifier);
		return "Changes are applied";
	}

	@Override
	public UserBasicInfo getUserBasicInfoByUsername(String username) {
		UserBasicInfo userBasicInfo = userRepository.getUserBasicInfoByUsername(username)
				.orElseThrow(()->new UserException("No user is available with username: "+username));
		return userBasicInfo;
	}
}
