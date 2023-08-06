package com.Insightgram.services;

import java.io.IOException;

import com.Insightgram.dto.PostDto;
import com.Insightgram.entities.forms.PostCreationForm;
import com.Insightgram.models.PageOf;

public interface PostService {
	
	PostDto createNewPost(PostCreationForm postCreationForm)throws IOException ;
	PostDto viewPostById(Integer postId) throws IllegalAccessException;
	Boolean deletePostById(Integer postId) throws IllegalAccessException;
	Boolean editPostCaptionById(Integer postId, String caption) throws IllegalAccessException;
	
	PageOf<PostDto> getPostsOfUser(Integer userId, Integer pageSize, Integer pageNumber);
	PageOf<PostDto> getNewsFeedContent(Integer pageSize, Integer pageNumber);
	
}