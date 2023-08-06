package com.Insightgram.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.Insightgram.dto.UserBasicInfo;
import com.Insightgram.enties.StoryViewer;

public interface StoryViewerRepository extends JpaRepository<StoryViewer, Integer>, PagingAndSortingRepository<StoryViewer, Integer>{
	@Query("Select new com.Insightgram.dto.UserBasicInfo(viewer) From StoryViewer Where story.storyId = :storyId Order By viewDateTime")
	Page<UserBasicInfo> seeViewers(Integer storyId, Pageable pageable);
	
	@Query("SELECT COUNT(v) FROM StoryViewer v WHERE v.story.storyId = :storyId AND v.viewer.userId = :viewerId")
	int isAlreadyAViewer(Integer storyId, Integer viewerId);
	
	@Modifying
	@Query("DELETE FROM StoryViewer v WHERE v.story.storyId = :storyId")
	int deleteViewersOfStory(Integer storyId);
}
