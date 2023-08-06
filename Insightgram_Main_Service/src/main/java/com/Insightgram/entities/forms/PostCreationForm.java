package com.Insightgram.entities.forms;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.enties.enums.PostType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreationForm {
	
	@NotNull
	@JsonIgnore
	private Set<MultipartFile> content = new LinkedHashSet<>();
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PostType postType;
	
	private String caption;
	
	public PostCreationForm(MultipartFile[] files, PostType postType, String caption) {
		for(MultipartFile file: files) {
			content.add(file);
		}
		this.postType = postType;
		this.caption = caption;
	}
}
