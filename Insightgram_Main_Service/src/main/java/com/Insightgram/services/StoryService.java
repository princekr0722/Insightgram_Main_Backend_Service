package com.Insightgram.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.dto.StoryDto;
import com.Insightgram.dto.UserBasicInfo;
import com.Insightgram.models.PageOf;

public interface StoryService {
	
	StoryDto postStory(MultipartFile file) throws IllegalStateException, IOException;
	String deleteStory(Integer storyId);
	ContentByteAndType viewStory(Integer storyId) throws IOException;
	PageOf<UserBasicInfo> seeViewers(Integer storyId, Integer pageSize, Integer pageNumber);
	List<StoryDto> getStoriesOfUser(Integer userId);
	
	List<UserBasicInfo> followingsWhoHaveStories();
	
	int cleanUpStoriesOlderThanADay();
	
}
