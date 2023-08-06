package com.Insightgram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Insightgram.dto.PostCommentDto;
import com.Insightgram.dto.PostLikeDto;
import com.Insightgram.models.PageOf;
import com.Insightgram.services.PostCommentService;
import com.Insightgram.services.PostLikeService;
import com.Insightgram.services.SortType;

@RestController
@RequestMapping("/main-app")
public class LikeCommentShareController {
	
	@Autowired
	private PostLikeService postLikeService;
	
	@Autowired
	private PostCommentService postCommentService;

	@GetMapping("/post/{postId}/like")
	public String likePost(@PathVariable Integer postId) throws IllegalAccessException {
		return postLikeService.likePost(postId);
	}
	
	@GetMapping("/post/{postId}/unlike")
	public String unlikePost(@PathVariable Integer postId) {
		return postLikeService.unlikePost(postId);
	}
	
	@GetMapping("/post/{postId}/likes")
	public ResponseEntity<PageOf<PostLikeDto>> getLikesOfPost(@PathVariable Integer postId, @RequestParam Integer pageSize, @RequestParam Integer pageNumber){
		pageNumber--;
		return ResponseEntity.status(HttpStatus.OK)
				.body(postLikeService.getAllPostLikes(postId, pageSize, pageNumber));
	}
	
	@GetMapping("/post/{postId}/isLiked")
	public ResponseEntity<Boolean> haveLikedAlready(@PathVariable Integer postId) {
		boolean haveLiked = postLikeService.havedUserLikedThePostAlready(postId);
		return new ResponseEntity<Boolean>(haveLiked, HttpStatus.OK);
	}
	
	@PostMapping("/post/{postId}")
	public PostCommentDto commentOnPost(@PathVariable Integer postId, @RequestParam String comment) throws IllegalAccessException {
		return postCommentService.commentOnPost(postId, comment);
	}
	
	@DeleteMapping("/post/{postId}/comment/{commentId}")
	public ResponseEntity<String> commentDeleted(@PathVariable Integer postId, @PathVariable Integer commentId) {
		return new ResponseEntity<String>(postCommentService.deleteCommentFromPost(postId, commentId), HttpStatus.OK);
	}
	
	//TODO get page wise comments
	@GetMapping("/post/{postId}/comments")
	public ResponseEntity<PageOf<PostCommentDto>> getCommentsOfPost(@PathVariable Integer postId, @RequestParam Integer pageSize, @RequestParam Integer pageNumber, @RequestParam SortType sortType){
		pageNumber--;
		return new ResponseEntity<PageOf<PostCommentDto>>(postCommentService.getCommentsOfPost(postId, pageSize, pageNumber, sortType), HttpStatus.OK);
	}
	
}
