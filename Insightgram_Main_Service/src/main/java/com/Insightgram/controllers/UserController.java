package com.Insightgram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Insightgram.dto.UserBasicInfo;
import com.Insightgram.dto.UserDto;
import com.Insightgram.dto.UserProfileDto;
import com.Insightgram.enties.User;
import com.Insightgram.entities.forms.EditProfileForm;
import com.Insightgram.entities.forms.PersonalInfoForm;
import com.Insightgram.models.PageOf;
import com.Insightgram.services.SortType;
import com.Insightgram.services.SortUserBy;
import com.Insightgram.services.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/main-app")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/hello")
	public String sayHello() {
		return "hello";
	}
	
	@DeleteMapping("/user")
	public User userDeletion(@RequestHeader String password) throws IllegalAccessException {
		return userService.deleteUser(password);
	}
	
	@GetMapping("/user/{userId}/profile")
	public ResponseEntity<UserDto> getUser(@PathVariable Integer userId) {
		return new ResponseEntity<UserDto>(userService.getPublicProfile(userId), HttpStatus.OK);
	}
	
	@GetMapping("/myProfile")
	public ResponseEntity<UserProfileDto> getMyProfile(){
		return new ResponseEntity<UserProfileDto>(userService.getMyProfile(), HttpStatus.OK);
	}
	
	@GetMapping("users/search/{keyword}")
	public ResponseEntity<PageOf<UserBasicInfo>> searchUserPageWise(@PathVariable String keyword, @RequestParam Integer pageSize, @RequestParam Integer pageNumber, @RequestParam(required = false) SortUserBy sortBy,
			@RequestParam(required = false) SortType sortType){
		
		pageNumber--;
		if(sortBy !=null && sortType!=null) {
			PageOf<UserBasicInfo> searchResult = userService.searchUserPageWise(keyword, pageSize, pageNumber, sortBy, sortType);
			return new ResponseEntity<>(searchResult, HttpStatus.OK);
		}else if(sortBy!=null && sortType==null) {
			PageOf<UserBasicInfo> searchResult = userService.searchUserPageWise(keyword, pageSize, pageNumber, sortBy);
			return new ResponseEntity<>(searchResult, HttpStatus.OK);
		}else {
			PageOf<UserBasicInfo> searchResult = userService.searchUserPageWise(keyword, pageSize, pageNumber);
			return new ResponseEntity<>(searchResult, HttpStatus.OK);
		}
	}
	
	@GetMapping("/user/account/visiblity")
	public ResponseEntity<String> changeAccountVisiblity(){
		return new ResponseEntity<String>(userService.changeAccountVisiblity() ,HttpStatus.OK);
	}
	
	@PutMapping("/user/personalInfo")
	public ResponseEntity<String> changePersonalInfo(@Valid @RequestBody PersonalInfoForm basicUserInfoForm) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.changeBasicUserInfo(basicUserInfoForm));
	}
	
	@PutMapping("/user/profileInfo")
	public ResponseEntity<String> editProfile(@Valid @RequestBody EditProfileForm editProfileForm){
		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.editProfile(editProfileForm));
	}
	
	@GetMapping("/user/{username}/info")
	public ResponseEntity<UserBasicInfo> getUserBasicInfoByUsername(@PathVariable String username) {
		UserBasicInfo userBasicInfo = userService.getUserBasicInfoByUsername(username);
		return new ResponseEntity<UserBasicInfo>(userBasicInfo, HttpStatus.OK);
	}
}