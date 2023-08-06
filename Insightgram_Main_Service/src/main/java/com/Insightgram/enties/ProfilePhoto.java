package com.Insightgram.enties;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilePhoto {
	
	@Id
	@GeneratedValue
	private Integer profilePhotoId;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;
	
	@Column(nullable = false)
	private String mediaType;
	
	@Column(nullable = false)
	@JsonIgnore
	private String imagePath;
	
	@JsonIgnore
	@Column(nullable = false)
	private final LocalDateTime profilePhotoDateTime = LocalDateTime.now();
	
	public static final String[] SUPPORTED_IMAGE_FORMATS = {"image/jpeg", "image/png"};
	
	public static boolean isValidImageFormat(String contentType) {
		for(String imageFormat: SUPPORTED_IMAGE_FORMATS) {
			if(contentType.equals(imageFormat)) return true;
		}
		return false;
	}
}
