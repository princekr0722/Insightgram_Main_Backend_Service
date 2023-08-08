package com.Insightgram.enties;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.Insightgram.dto.UploadedFileDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Story {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int storyId;
	
	@Column(nullable = false, unique = true)
	@JsonIgnore
	private String storyPublicId;
	
	@Column(nullable = false, unique = true)
	private String storyContentURL;
	
	@Column(nullable = false)
	private String storyContentType; 
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(nullable = false)
	private LocalDateTime storyDateTime = LocalDateTime.now();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "story")
	private Set<StoryViewer> viewers = new HashSet<>();

	public Story(UploadedFileDetails uploadedFileDetails, User user) {
		storyPublicId = uploadedFileDetails.getPublicId();
		storyContentURL = uploadedFileDetails.getUrl();
		storyContentType = uploadedFileDetails.getResourceType();
		this.user = user;
	}
	
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
	
	public static String getSupportedMediaType() {
		StringBuilder sb = new StringBuilder();
		for(String mt: SUPPORTED_IMAGE_FORMATS) {
			sb.append(mt+", ");
		}
		for(String mt: SUPPORTED_VIDEO_FORMATS) {
			sb.append(mt+", ");
		}
		sb.setLength(sb.length()-2);
		return sb.toString();
	}

}
