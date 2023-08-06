package com.Insightgram.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.Insightgram.enties.Content;
import com.Insightgram.enties.Post;
import com.Insightgram.enties.User;
import com.Insightgram.enties.enums.PostType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostAllDetails {
	private int postId;
	
	private User user;

	private Set<Content> content = new LinkedHashSet<>();
	
	private PostType postType;
	
	private String caption;
	
	private LocalDateTime postDateTime;
	
	
	private List<PostLikeDto> likes = new ArrayList<>();
	
	private List<PostCommentDto> comments = new ArrayList<>();

	public PostAllDetails(Post post) {
		this.postId= post.getPostId();
		this.user = post.getUser();
		this.postType = post.getPostType();
		this.content = post.getContent();
		this.caption = post.getCaption();
		this.postDateTime = post.getPostDateTime();
	}
	
}
