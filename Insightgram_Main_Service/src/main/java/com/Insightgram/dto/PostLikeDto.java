package com.Insightgram.dto;

import java.time.LocalDateTime;

import com.Insightgram.enties.PostLike;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostLikeDto extends UserBasicInfo {
	private Integer likeId;
	private Integer postId;
	private LocalDateTime likeDataTime;
	
	private UserBasicInfo likerInfo;

	public PostLikeDto(PostLike like) {
		super(like.getUser());
		likeId = like.getLikeId();
		postId = like.getPost().getPostId();
		likeDataTime = like.getLikeDateTime();
	
		likerInfo = new UserBasicInfo(like.getUser());
	}

}
