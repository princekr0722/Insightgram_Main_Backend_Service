package com.Insightgram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Insightgram.dto.PostLikeDto;
import com.Insightgram.enties.Post;
import com.Insightgram.enties.PostLike;
import com.Insightgram.enties.User;
import com.Insightgram.models.PageOf;
import com.Insightgram.repositories.PostLikeRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PostLikeServiceImpl implements PostLikeService {

	@Autowired
	private PostLikeRepository postLikeRepository;
	
	@Autowired
	private UserUtilServices userUtilServices;
	
	@Autowired
	private PostUtilServices postUtilServices;
	
	@Override
	public String likePost(Integer postId) throws IllegalAccessException {
		User currentUser = userUtilServices.getCurrentUser();
		Post post = postUtilServices.getPostById(postId);
		
		validateLike(currentUser.getUserId(), post);
		
		PostLike like = new PostLike(post, currentUser);
		postLikeRepository.save(like);
		
		return "Liked the post with Id: "+postId;
	}
	
	private void validateLike(Integer currentUserId, Post post) throws IllegalAccessException {
		User postOwner = post.getUser();
		
		// check1 is for verifying if the post if of private account
		boolean check1 = userUtilServices.canInteractWithAnotherUser(currentUserId, postOwner);
		
		if(!check1) throw new IllegalAccessException(postUtilServices.ILLEGAL_POST_ACCESS_MSG);
		
		// check2 is for verifying if the user has already liked the post
		boolean check2 = false; 
		if(postLikeRepository.haveLiked(post.getPostId(), currentUserId).isEmpty()) check2 = true;
		else check2 = false;
		
		if(!check2) throw new IllegalArgumentException("Cannot like a post more than once.");
	}

	@Override
	@Transactional
	public String unlikePost(Integer postId) {
		int rowDeleted = postLikeRepository.unlikePost(postId, userUtilServices.getCurrentUser().getUserId());
		if(rowDeleted == 0) throw new IllegalArgumentException("you cannot unlike if you haven't liked the post.");
		return "Unliked the post with Id: "+postId;
	}
	
	@Override
	public boolean havedUserLikedThePostAlready(Integer postId) {
		Integer userId = userUtilServices.getUserIdByUsernameOrMobile(SecurityContextHolder.getContext().getAuthentication().getName());
		int haveLiked= postLikeRepository.havedUserLikedThePostAlready(postId, userId);
		if(haveLiked == 0) return false;
		else return true;
	}

	@Override
	public Integer getLikeCount(Integer postId) {
		return postLikeRepository.likeCount(postId);
	}

	@Override
	public PageOf<PostLikeDto> getAllPostLikes(Integer postId, Integer pageSize, Integer pageNumber) {
		if(pageNumber<0) throw new IllegalArgumentException("Page index must not be less than or equals to zero");
		
		User currentUser = userUtilServices.getCurrentUser();
		Post post = postUtilServices.getPostById(postId);
		if(!userUtilServices.canInteractWithAnotherUser(currentUser.getUserId(), post.getUser())) {
			throw new IllegalArgumentException(postUtilServices.ILLEGAL_POST_ACCESS_MSG);
		}else {
			Sort sort = Sort.by("likeDateTime").descending();
			
			Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
			return new PageOf<>(postLikeRepository.getAllPostLikes(postId, pageable));
		}
	}

}
