package com.Insightgram.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.dto.PostDto;
import com.Insightgram.enties.enums.PostType;
import com.Insightgram.entities.forms.PostCreationForm;
import com.Insightgram.models.PageOf;
import com.Insightgram.services.PostService;

@RestController
@RequestMapping("/main-app")
public class PostController {
	
		@Autowired
		private PostService postService;
		
		@PostMapping("/post")
		public ResponseEntity<PostDto> createPost(@RequestParam MultipartFile[] file,
								@RequestParam PostType postType,
								@RequestParam String caption) throws IOException {
			
			PostCreationForm postCreationForm = new PostCreationForm(file, postType, caption);
			return new ResponseEntity<PostDto>(postService.createNewPost(postCreationForm), HttpStatus.CREATED);
		}
		
		@GetMapping("/post/{postId}")
		public ResponseEntity<PostDto> viewPostById(@PathVariable Integer postId) throws IllegalAccessException {
			return new ResponseEntity<PostDto>(postService.viewPostById(postId), HttpStatus.OK);
		}
		
		@DeleteMapping("user/post/{postId}")
		public ResponseEntity<Boolean> deletePostById(@PathVariable Integer postId) throws IllegalAccessException {
			return new ResponseEntity<Boolean>(postService.deletePostById(postId), HttpStatus.OK);
		}
		
		@PatchMapping("/user/post/{postId}")
		public ResponseEntity<Boolean> editPostCaptionById(@PathVariable Integer postId,@RequestParam String caption) throws IllegalAccessException {
			return new ResponseEntity<Boolean>(postService.editPostCaptionById(postId, caption), HttpStatus.OK);
		}
		
		@GetMapping("user/{userId}/posts")
		ResponseEntity<PageOf<PostDto>> getPostsOfUser(@PathVariable Integer userId, @RequestParam Integer pageSize, @RequestParam Integer pageNumber){
			pageNumber--;
			return new ResponseEntity<PageOf<PostDto>>(postService.getPostsOfUser(userId, pageSize, pageNumber), HttpStatus.OK);
		}
	
}
