package com.Insightgram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Insightgram.dto.UserBasicInfo;
import com.Insightgram.models.PageOf;
import com.Insightgram.services.UserFollowsService;

@RestController
@RequestMapping("/main-app")
public class FolloweeAndFollowerController {

	@Autowired
	private UserFollowsService userFollowsService;
	
	@GetMapping("/user/{userId}/follow")
	public ResponseEntity<String> followUser(@PathVariable Integer userId){
		return new ResponseEntity<String>(userFollowsService.followUser(userId), HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}/unfollow")
	public ResponseEntity<String> unfollowUser(@PathVariable Integer userId){
		return new ResponseEntity<String>(userFollowsService.unfollowUser(userId), HttpStatus.OK);
	}
	
	@DeleteMapping("user/follower/{followerId}")
	public ResponseEntity<String> removeFollower(@PathVariable Integer followerId){
		return new ResponseEntity<>(userFollowsService.removeFollower(followerId), HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}/isfollower/check")
	public ResponseEntity<Boolean> isUserAFollowerOf(@PathVariable Integer userId) {
		return new ResponseEntity<Boolean>(userFollowsService.isUserAFollowerOf(userId), HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}/followers")
	public ResponseEntity<PageOf<UserBasicInfo>> getUserFollowers(@PathVariable Integer userId, @RequestParam Integer pageSize, @RequestParam Integer pageNumber){
		pageNumber--;
		return new ResponseEntity<>(userFollowsService.getUserFollowers(userId, pageSize, pageNumber), HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}/followings")
	public ResponseEntity<PageOf<UserBasicInfo>> getUserFollowings(@PathVariable Integer userId, @RequestParam Integer pageSize, @RequestParam Integer pageNumber){
		pageNumber--;
		return new ResponseEntity<>(userFollowsService.getUserFollowings(userId, pageSize, pageNumber), HttpStatus.OK);
	}
	
}
