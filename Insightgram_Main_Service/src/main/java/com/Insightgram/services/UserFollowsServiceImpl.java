package com.Insightgram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Insightgram.dto.UserBasicInfo;
import com.Insightgram.enties.User;
import com.Insightgram.enties.enums.AccountType;
import com.Insightgram.exceptions.UserException;
import com.Insightgram.models.PageOf;
import com.Insightgram.repositories.UserFollowsRepository;
import com.Insightgram.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserFollowsServiceImpl implements UserFollowsService {

	@Autowired
	private UserFollowsRepository userFollowsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserUtilServices userUtilServices;

	@Override
	public PageOf<UserBasicInfo> getUserFollowings(Integer userId, Integer pageSize, Integer pageNumber) {
		if (pageNumber < 0)
			throw new IllegalArgumentException("Page index must not be less than or equals to zero");

		User userToAccess = userUtilServices.getUserById(userId);
		Integer currentUserId = userUtilServices
				.getUserIdByUsernameOrMobile(SecurityContextHolder.getContext().getAuthentication().getName());

		if (!userUtilServices.canInteractWithAnotherUser(currentUserId, userToAccess)) {
			throw new UserException(
					"User cannot access private account details if user is not a follower of private account");
		} else {
			Pageable pageable = PageRequest.of(pageNumber, pageSize);
			return new PageOf<>(userFollowsRepository.getUserFollowing(userId, pageable));
		}
	}

	@Override
	public PageOf<UserBasicInfo> getUserFollowers(Integer userId, Integer pageSize, Integer pageNumber) {
		if (pageNumber < 0)
			throw new IllegalArgumentException("Page index must not be less than or equals to zero");

		User userToAccess = userUtilServices.getUserById(userId);
		Integer currentUserId = userUtilServices
				.getUserIdByUsernameOrMobile(SecurityContextHolder.getContext().getAuthentication().getName());

		if (!userUtilServices.canInteractWithAnotherUser(currentUserId, userToAccess)) {
			throw new UserException(
					"User cannot access private account details if user is not a follower of private account");
		} else {
			Pageable pageable = PageRequest.of(pageNumber, pageSize);
			return new PageOf<>(userFollowsRepository.getUserFollowers(userId, pageable));
		}

	}

	@Override
	public String followUser(Integer userId) {

		User userToFollow = userUtilServices.getUserById(userId);
		String currentUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
		if (userToFollow.getUsername().equals(currentUserIdentifier)
				|| userToFollow.getMobileNumber().equals(currentUserIdentifier)) {
			throw new UserException("Users cannot follow themselves");
		} else {
			if (userToFollow.getAccountType() == AccountType.PRIVATE) {
				privateAccountFollow(userToFollow, currentUserIdentifier);
				// TODO notify the followee about new follower request
//				return "sent follow request to user with Id: " + userId;
//				return "Option to follow private accounts is not available yet";
				throw new UserException("Option to follow private accounts is not available yet");
			} else {
				publicAccountFollow(userToFollow, currentUserIdentifier);
				// TODO notify the followee about new follower
				return "following user with Id: " + userId;
			}
		}
	}

	private void privateAccountFollow(User userToFollow, String currentUserIdentifier) {
		Integer currentUserId = userUtilServices.getUserIdByUsernameOrMobile(currentUserIdentifier);

		if (ifFollows(userToFollow.getUserId(), currentUserId)) {
			throw new UserException("Already a follower of user with Id: " + userToFollow.getUserId());
		}
	}

	private void publicAccountFollow(User userToFollow, String currentUserIdentifier) {
		Integer currentUserId = userUtilServices.getUserIdByUsernameOrMobile(currentUserIdentifier);

		if (ifFollows(userToFollow.getUserId(), currentUserId)) {
			throw new UserException("Already a follower of user with Id: " + userToFollow.getUserId());
		}

		User currentUser = userUtilServices.getCurrentUser();
		currentUser.getFollowing().add(userToFollow);
		userRepository.save(currentUser);
	}

	private boolean ifFollows(Integer followeeId, Integer followerId) {
		int follows = userFollowsRepository.checkUserFollows(followeeId, followerId);
		if (follows == 0)
			return false;
		else
			return true;
	}

	@Override
	public String unfollowUser(Integer userId) {
		Integer currentUserId = userUtilServices
				.getUserIdByUsernameOrMobile(SecurityContextHolder.getContext().getAuthentication().getName());
		if (currentUserId == userId)
			throw new UserException("Users cannot unfollow themselves");

		Integer unfollowed = userFollowsRepository.unfollow(userId, currentUserId);
		if (unfollowed == 0)
			throw new UserException("Not a follower of user with Id: " + userId);
		else
			return "Unfollowed user with Id: " + userId;
	}

	@Override
	public String removeFollower(Integer followerId) {
		Integer currentUserId = userUtilServices
				.getUserIdByUsernameOrMobile(SecurityContextHolder.getContext().getAuthentication().getName());
		if (currentUserId == followerId)
			throw new UserException("Users cannot remove themselves from followers");

		Integer unfollowed = userFollowsRepository.unfollow(currentUserId, followerId);
		if (unfollowed == 0)
			throw new UserException("No follower exists with Id: " + followerId);
		else
			return "User with Id " + followerId + " has been removed from follower list";
	}
	
	@Override
	public Boolean isUserAFollowerOf(Integer userId) {
		Integer currentUserId = userUtilServices.getUserIdByUsernameOrMobile(SecurityContextHolder.getContext().getAuthentication().getName());
		return ifFollows(userId, currentUserId);
	}
}
