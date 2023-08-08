package com.Insightgram.services;

import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.dto.UploadedFileDetails;

public interface CloudinaryService {
	
	UploadedFileDetails uploadMedia(MultipartFile file);
	public Boolean deleteMedia(String publicId, String resourceType);
	
}
