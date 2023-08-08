package com.Insightgram.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.dto.ContentUrlAndType;

public interface ProfilePhotoService {
	
	ContentUrlAndType viewProfilePhoto(String uniqueUserIndetifier) throws IOException;
	
	String addProfilePhoto(MultipartFile file) throws IllegalStateException, IOException ;
	
	String changeProfilePhoto(MultipartFile file) throws IllegalStateException, IOException ;
	
	String removeProfilePhoto();
	
}
