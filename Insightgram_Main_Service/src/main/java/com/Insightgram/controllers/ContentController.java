package com.Insightgram.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Insightgram.services.ContentByteAndType;
import com.Insightgram.services.ContentService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/main-app")
public class ContentController {

		@Autowired
		private ContentService contentService;
		
		@GetMapping("/post/content/{contentId}")
		public ResponseEntity<?> viewMediaOfContent(@PathVariable Integer contentId) throws IOException{
	        
			ContentByteAndType contentByteAndType = contentService.viewContent(contentId);

	        return ResponseEntity.status(HttpStatus.OK)
					.contentType(MediaType.valueOf(contentByteAndType.getMediaType()))
					.body(contentByteAndType.getMediaBytes());
		}
		
		@GetMapping(value = "/video/{fileName}", produces = "*")
	    public Mono<Resource> getVideos(@PathVariable Integer contentId, @RequestHeader String range) {
	        return contentService.streamLiveVideo(contentId);
	    }
	
}
