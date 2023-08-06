package com.Insightgram.dto;

import java.time.LocalDateTime;

import com.Insightgram.enties.Post;
import com.Insightgram.enties.PostComment;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentDto extends UserBasicInfo{
	private Integer commentId;
	private String comment;
	@JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
	private LocalDateTime commentDateTime;
	
	private Integer postId;
	private UserBasicInfo commentOwner;
	
	public PostCommentDto(PostComment comment) {
		super(comment.getUser());
		
		Post post = comment.getPost();
		this.commentId = comment.getCommentId();
		this.comment = comment.getComment();
		this.commentDateTime = comment.getCommentDateTime();
		this.postId = post.getPostId();	
		this.commentOwner = new UserBasicInfo(comment.getUser());
	}
}
