package com.Insightgram.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.dto.StoryDto;
import com.Insightgram.dto.UserBasicInfo;
import com.Insightgram.models.PageOf;
import com.Insightgram.services.ContentByteAndType;
import com.Insightgram.services.StoryService;

@RestController
@RequestMapping("/main-app")
public class StoryController {
	
	@Autowired
	private StoryService storyService;
	
	@PostMapping("/story")
	ResponseEntity<StoryDto> postStory(@RequestParam MultipartFile file) throws IllegalStateException, IOException{
		return new ResponseEntity<StoryDto>(storyService.postStory(file), HttpStatus.OK);
	}
	
	@DeleteMapping("/story/{storyId}")
	ResponseEntity<String> deleteStory(@PathVariable Integer storyId){
		return new ResponseEntity<String>(storyService.deleteStory(storyId), HttpStatus.OK);
	}
	
	@GetMapping("/story/{storyId}")
	ResponseEntity<?> viewStory(@PathVariable Integer storyId) throws IOException{
		ContentByteAndType byteAndType = storyService.viewStory(storyId);
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.valueOf(byteAndType.getMediaType()))
				.body(byteAndType.getMediaBytes());
	}
	
	@GetMapping("/user/{userId}/stories")
	ResponseEntity<List<StoryDto>> getStoriesOfUser(@PathVariable Integer userId){
		return new ResponseEntity<List<StoryDto>>(storyService.getStoriesOfUser(userId), HttpStatus.OK);
	}
	
	@GetMapping("/story/{storyId}/views")
	ResponseEntity<PageOf<UserBasicInfo>> seeViewers(@PathVariable Integer storyId, @RequestParam Integer pageSize, @RequestParam Integer pageNumber){
		PageOf<UserBasicInfo> viewers = storyService.seeViewers(storyId, pageSize, pageNumber);
		return new ResponseEntity<PageOf<UserBasicInfo>>(viewers, HttpStatus.OK);
	}
	
	@GetMapping("/following/stories")
	ResponseEntity<List<UserBasicInfo>> usersWhoHaveStory(){
		return new ResponseEntity<List<UserBasicInfo>>(storyService.followingsWhoHaveStories(), HttpStatus.OK);
	}
}
