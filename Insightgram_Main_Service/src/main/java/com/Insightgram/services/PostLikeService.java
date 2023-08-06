package com.Insightgram.services;

import com.Insightgram.dto.PostLikeDto;
import com.Insightgram.models.PageOf;

public interface PostLikeService {
	String likePost(Integer postId) throws IllegalAccessException ;
	String unlikePost(Integer postId);
	boolean havedUserLikedThePostAlready(Integer postId);
	Integer getLikeCount(Integer postId);
	PageOf<PostLikeDto> getAllPostLikes(Integer postId, Integer pageSize, Integer pageNumber);
}
