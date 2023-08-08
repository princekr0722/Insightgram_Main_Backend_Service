package com.Insightgram.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UploadedFileDetails {
	private String publicId;
	private String url;
	private String resourceType;
}
