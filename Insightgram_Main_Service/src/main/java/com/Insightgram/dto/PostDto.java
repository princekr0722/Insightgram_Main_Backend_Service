package com.Insightgram.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.Insightgram.enties.Content;
import com.Insightgram.enties.Post;
import com.Insightgram.enties.enums.PostType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {

	private int postId;
	UserBasicInfo postOwnerInfo; 
	private Set<Content> content;
	private PostType postType;
	private String caption;
	@JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
	private LocalDateTime postDateTime;
	private int noOfLikes;
	private int noOfComments;

	public PostDto(Post post) {
		
		this.postOwnerInfo = new UserBasicInfo(post.getUser());
		this.postId = post.getPostId();
		this.content = post.getContent();
		this.postType = post.getPostType();
		this.caption = post.getCaption();
		this.postDateTime = post.getPostDateTime();
		this.noOfLikes = post.getLikes().size();
		this.noOfComments = post.getComments().size();
	}

	@Override
	public String toString() {
		return "postId=" + postId + "\npostOwnerInfo=" + postOwnerInfo + "\npostType=" + postType
				+ "\ncaption=" + caption + "\npostDateTime=" + postDateTime + "\nnoOfLikes=" + noOfLikes
				+ "\nnoOfComments=" + noOfComments;
	}
	
	
}
