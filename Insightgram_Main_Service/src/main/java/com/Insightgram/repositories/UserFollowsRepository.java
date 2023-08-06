package com.Insightgram.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.Insightgram.dto.UserBasicInfo;
import com.Insightgram.dto.UserFollowsDto;
import com.Insightgram.enties.UserFollows;
import com.Insightgram.enties.UserFollowsId;

public interface UserFollowsRepository extends JpaRepository<UserFollows, UserFollowsId>{
	@Query("Select new com.Insightgram.dto.UserFollowsDto(id.followee.userId, id.follower.userId) From UserFollows Where id.followee.userId = :followeeId AND id.follower.userId = :followerId")
	UserFollowsDto getUserFollowsDto(Integer followeeId, Integer followerId);
	
	@Query("Select Count(uf) From UserFollows as uf Where uf.id.followee.userId = :followeeId AND uf.id.follower.userId = :followerId")
	Integer checkUserFollows(Integer followeeId, Integer followerId);
	
//	@Modifying
//	@Query("Insert Into UserFollows (id.followee.userId, id.follower.userId) Values (:followeeId, :followerid)")
//	Integer follow(Integer followeeId, Integer followerId);
	
	@Modifying
	@Query("Delete From UserFollows uf Where uf.id.followee.userId = :followeeId AND uf.id.follower.userId = :followerId")
	Integer unfollow(Integer followeeId, Integer followerId);
	
	@Query("Select new com.Insightgram.dto.UserBasicInfo(id.followee) From UserFollows Where id.follower.userId = :userId")
	Page<UserBasicInfo> getUserFollowing(Integer userId, Pageable pageable);

	@Query("Select new com.Insightgram.dto.UserBasicInfo(id.follower) From UserFollows Where id.followee.userId = :userId")
	Page<UserBasicInfo> getUserFollowers(Integer userId, Pageable pageable);
	
	//TODO return followers/followees in pages
	
}
