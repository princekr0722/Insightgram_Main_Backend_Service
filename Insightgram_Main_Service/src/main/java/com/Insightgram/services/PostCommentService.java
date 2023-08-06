package com.Insightgram.services;

import com.Insightgram.dto.PostCommentDto;
import com.Insightgram.models.PageOf;

public interface PostCommentService {
	
	PostCommentDto commentOnPost(Integer postId, String comment) throws IllegalAccessException;
	String deleteCommentFromPost(Integer postId, Integer commentId);
	int countCommentsOnPost(Integer postId);
	PageOf<PostCommentDto> getCommentsOfPost(Integer postId, Integer pageSize, Integer pageNumber, SortType sortType);
	
}
