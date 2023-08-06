package com.Insightgram.dto;

import java.time.LocalDateTime;

import com.Insightgram.enties.Story;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoryDto {
	
	private int storyId;
	private String contentType;
	@JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
	private LocalDateTime storyDateTime;
	private int viewCount;
	
	public StoryDto(Story story) {
		this.storyId = story.getStoryId();
		this.contentType = story.getStoryContentType();
		this.storyDateTime = story.getStoryDateTime();
		this.viewCount = story.getViewers().size();
	}
}
