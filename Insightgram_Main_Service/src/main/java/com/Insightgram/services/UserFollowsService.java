package com.Insightgram.services;

import com.Insightgram.dto.UserBasicInfo;
import com.Insightgram.models.PageOf;

public interface UserFollowsService {
	String followUser(Integer userId);
	String unfollowUser(Integer userId);
	String removeFollower(Integer followerId);
	Boolean isUserAFollowerOf(Integer userId);
	
	PageOf<UserBasicInfo> getUserFollowings(Integer userId, Integer pageSize, Integer pageNumber);
	PageOf<UserBasicInfo> getUserFollowers(Integer userId, Integer pageSize, Integer pageNumber);
}
