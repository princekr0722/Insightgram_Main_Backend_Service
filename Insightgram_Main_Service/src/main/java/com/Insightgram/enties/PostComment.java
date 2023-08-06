package com.Insightgram.enties;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PostComment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty(access = Access.READ_ONLY)
	private int commentId;
	
	@Column(nullable = false, length = 500)
	private String comment;
	
	@ManyToOne
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;
	
	@ManyToOne
	@JoinColumn(name = "commented_user_id", nullable = false)
	private User user;
	
	@JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
	private LocalDateTime commentDateTime = LocalDateTime.now();
	
	public PostComment(Post post, User user, String comment) {
		this.comment = comment;
		this.post = post;
		this.user = user;
	}
	
}
