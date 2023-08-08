package com.Insightgram.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.Insightgram.dto.UserBasicInfo;
import com.Insightgram.enties.Story;

public interface StoryRepository extends JpaRepository<Story, Integer>{
	
	@Modifying
	@Query("DELETE FROM Story s where s.storyId = :storyId AND (s.user.username = :uniqueUserIdentifier OR s.user.mobileNumber = :uniqueUserIdentifier)")
	int deleteStoryById(Integer storyId, String uniqueUserIdentifier);
	
	@Query("Select count(s) From Story as s where s.storyId = :storyId AND (s.user.username = :uniqueUserIdentifier OR s.user.mobileNumber = :uniqueUserIdentifier)")
	int isStoryAvailable(String uniqueUserIdentifier, Integer storyId);
	
	@Query("Select new com.Insightgram.dto.UserBasicInfo(f) From User u Inner Join u.following f Where (u.username = :uniqueUserIdentifier Or u.mobileNumber = :uniqueUserIdentifier) AND (Select Count(s) From f.stories s)>0")
	List<UserBasicInfo> followingsWhoHaveStories(String uniqueUserIdentifier);
	
	@Query("SELECT s.storyId, s.storyPublicId, s.storyContentType FROM Story s WHERE timestampdiff(hour, s.storyDateTime, current_timestamp()) >= 24")
	List<Object[]> get24HrsOldStoryIdsPublicIdAndContentType(LocalDateTime currentDateTime);
	
	@Modifying
	@Query("DELETE FROM Story s WHERE timestampdiff(hour, s.storyDateTime, current_timestamp()) >= 24")
	int delete24HrsOldStories(LocalDateTime currentDateTime);
	
	@Query("SELECT storyPublicId, storyContentType FROM Story WHERE storyId = :storyId")
	Optional<String> getPublicIdAndContentTypeOfStory(Integer storyId);
}
