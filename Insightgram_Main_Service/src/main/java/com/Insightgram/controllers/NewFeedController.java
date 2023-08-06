package com.Insightgram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Insightgram.dto.PostDto;
import com.Insightgram.models.PageOf;
import com.Insightgram.services.PostService;

@RestController
@RequestMapping("/main-app")
public class NewFeedController {
	
	@Autowired
	private PostService postService;
	
	@GetMapping("/newsFeed/posts")
	ResponseEntity<PageOf<PostDto>> getNewsFeedContent(@RequestParam Integer pageSize,@RequestParam Integer pageNumber){
		pageNumber--;
		return new ResponseEntity<>(postService.getNewsFeedContent(pageSize, pageNumber), HttpStatus.OK);
	}
	
}
