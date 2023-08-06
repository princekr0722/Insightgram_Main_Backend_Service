package com.Insightgram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Insightgram.enties.Post;
import com.Insightgram.repositories.PostRepository;

@Service
public class PostUtilServices {
	@Autowired
	private PostRepository postRepository;
	
	public final String ILLEGAL_POST_ACCESS_MSG = "Post owner's account is private, and you're not a follower of the post owner.";
	
	public Post getPostById(Integer postId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(()-> new IllegalArgumentException("No Post is availbale with id: "+postId));
		return post;
	}
	
	public Integer getPostOwnerId(Integer postId) {
		Integer postOwnerId = postRepository.getPostOwnerId(postId);
		return postOwnerId;
	}

}
