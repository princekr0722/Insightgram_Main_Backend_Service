package com.Insightgram.enties;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PostLike {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty(access = Access.READ_ONLY)
	private int likeId;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	@JsonIgnore
	private Post post;
	
	@ManyToOne
	@JoinColumn(name = "liked_user_id")
	private User user;
	
	@NotNull
	@JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
	private LocalDateTime likeDateTime = LocalDateTime.now();
	
	public PostLike(Post post, User user) {
		this.post = post;
		this.user = user;
	}

	@Override
	public String toString() {
		return "PostLike Id= " + likeId;
	}
}
