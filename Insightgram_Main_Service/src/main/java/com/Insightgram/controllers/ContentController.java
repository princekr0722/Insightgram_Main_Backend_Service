package com.Insightgram.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Insightgram.dto.ContentUrlAndType;
import com.Insightgram.services.ContentService;

@RestController
@RequestMapping("/main-app")
public class ContentController {

		@Autowired
		private ContentService contentService;
		
		@GetMapping("/post/content/{contentId}")
		public ResponseEntity<String> viewMediaOfContent(@PathVariable Integer contentId) throws IOException{
	        
			ContentUrlAndType contentUrlAndType = contentService.viewContent(contentId);

	        return ResponseEntity.status(HttpStatus.OK)
					.body(contentUrlAndType.getMediaUrl());
		}
		
//		@GetMapping(value = "/video/{fileName}", produces = "*")
//	    public Mono<Resource> getVideos(@PathVariable Integer contentId, @RequestHeader String range) {
//	        return contentService.streamLiveVideo(contentId);
//	    }
		
//		@GetMapping("/video")
//	    public ResponseEntity<String> getVideos(@PathVariable Integer contentId, @RequestHeader String range) {
//			ContentUrlAndType contentUrlAndType = contentService.streamLiveVideo(contentId);
//			return ResponseEntity.status(HttpStatus.OK)
//	        		.body(contentUrlAndType.getMediaUrl());
//	    }
}
