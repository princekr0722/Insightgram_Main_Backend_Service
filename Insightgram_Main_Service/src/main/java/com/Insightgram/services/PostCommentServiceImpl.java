package com.Insightgram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Insightgram.dto.PostCommentDto;
import com.Insightgram.enties.Post;
import com.Insightgram.enties.PostComment;
import com.Insightgram.enties.User;
import com.Insightgram.models.PageOf;
import com.Insightgram.repositories.PostCommentRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PostCommentServiceImpl implements PostCommentService{

	@Autowired
	private PostCommentRepository commentRepository;

	@Autowired
	private PostUtilServices postUtilServices;
	
	@Autowired
	private UserUtilServices userUtilServices;
	
	
	@Override
	public PostCommentDto commentOnPost(Integer postId, String comment) throws IllegalAccessException {
		Post post = postUtilServices.getPostById(postId);
		User postOwner = post.getUser();
		User currentUser = userUtilServices.getCurrentUser();
		validateComment(currentUser.getUserId(), postOwner);
		
		PostComment postComment = new PostComment(post, currentUser, comment);
		PostComment newComment = commentRepository.save(postComment);
		return new PostCommentDto(newComment);
	}

	private void validateComment(Integer currentUserId, User postOwner) throws IllegalAccessException {
		if(!userUtilServices.canInteractWithAnotherUser(currentUserId, postOwner) )
			throw new IllegalAccessException("Post owner's account is private, and you're not a follower of the post owner.");
		
	}
	
	@Override
	public String deleteCommentFromPost(Integer postId, Integer commentId) {
		Integer userId = userUtilServices.getUserIdByUsernameOrMobile(SecurityContextHolder.getContext().getAuthentication().getName());
		Integer postOwnerId = postUtilServices.getPostOwnerId(postId);
		
		int commentDeleted;
		if(postOwnerId != userId) 
			commentDeleted = commentRepository.deleteComment(commentId, postId, userId);
		else
			commentDeleted = commentRepository.deleteCommentByOwner(commentId, postId);
		
		if(commentDeleted==0) {
			throw new IllegalArgumentException("No comment found with commentId: "+commentId+" on post with postId: "+postId);
		}
		return "Comment Deleted";
	}

	@Override
	public int countCommentsOnPost(Integer postId) {
		return commentRepository.countCommentsOnPost(postId);
	}

	@Override
	public PageOf<PostCommentDto> getCommentsOfPost(Integer postId, Integer pageSize, Integer pageNumber, SortType sortType) {
		if(pageNumber<0) throw new IllegalArgumentException("Page index must not be less than or equals to zero");
		
		User currentUser = userUtilServices.getCurrentUser();
		Post post = postUtilServices.getPostById(postId);
		
		if(!userUtilServices.canInteractWithAnotherUser(currentUser.getUserId(), post.getUser())) {
			throw new IllegalArgumentException(postUtilServices.ILLEGAL_POST_ACCESS_MSG);
		}else {
			Sort sort;
			if(sortType == SortType.ASC) sort = Sort.by("commentDateTime").ascending();
			else sort = Sort.by("commentDateTime").descending();
			
			Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
			
			return new PageOf<>(commentRepository.getCommentsOfPost(postId, pageable));
			
		}
	}

	
	
}
