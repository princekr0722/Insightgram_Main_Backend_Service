package com.Insightgram.enties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Content {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty(access = Access.READ_ONLY)
	private int contentId;
	
	@NotBlank
	@Column(nullable = false)
	@JsonIgnore
	private String contentPublicId;
	
	@NotBlank
	@Column(nullable = false)
	private String contentType;
	
	@NotNull
	@Column(nullable = false)
	private String contentUrl;
	
	@ManyToOne
	@JoinColumn(name = "post_id")
	@JsonIgnore
	private Post post;
	
	public static final String[] SUPPORTED_VIDEO_FORMATS = {"video/mp4", "video/x-matroska"};
	public static final String[] SUPPORTED_IMAGE_FORMATS = {"image/jpeg", "image/png"};
	
	public static boolean isValidVideoFormat(String contentType) {
		for(String videoFormat: SUPPORTED_VIDEO_FORMATS) {
			if(contentType.equals(videoFormat)) return true;
		}
		return false;
	}
	
	public static boolean isValidImageFormat(String contentType) {
		for(String imageFormat: SUPPORTED_IMAGE_FORMATS) {
			if(contentType.equals(imageFormat)) return true;
		}
		return false;
	}
	
	public static boolean isValidContentType(String contentType) {
		return isValidVideoFormat(contentType) || isValidImageFormat(contentType);
	}
}
