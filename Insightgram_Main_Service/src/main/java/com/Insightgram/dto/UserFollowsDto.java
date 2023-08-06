package com.Insightgram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserFollowsDto {
	private int followeeId;
	private int followerId;
}
