package com.Insightgram.enties;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.Insightgram.enties.enums.PostType;
import com.Insightgram.entities.forms.PostCreationForm;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int postId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
	private Set<Content> content = new LinkedHashSet<>();
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PostType postType;
	
	@Column(length = 2000)
	private String caption;
	
	@NotNull
	@JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
	private LocalDateTime postDateTime = LocalDateTime.now();
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
	private Set<PostLike> likes = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
	private Set<PostComment> comments = new HashSet<>();

	public Post(User user, Set<Content> content, PostType postType, String caption) {
		this.user = user;
		this.postType = postType;
		if(postType == PostType.REEL) if(content.size()!=0) throw new IllegalArgumentException("Multiple reels can't be in one post.");
		this.content = content;
		this.caption = caption;
	}
	
	public Post(PostCreationForm form) {
		form.getCaption();
		form.getContent();
		form.getPostType();
	}
	
	public void addContent(Content content) {
		this.content.add(content);
	}

}
