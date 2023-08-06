package com.Insightgram.enties;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Embeddable
@Table(name = "user_follows")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class UserFollows {

	@EmbeddedId
	private UserFollowsId id;
	
	public UserFollows(User follower, User followee) {
		this.id = new UserFollowsId(follower, followee); 
	}
	
}
