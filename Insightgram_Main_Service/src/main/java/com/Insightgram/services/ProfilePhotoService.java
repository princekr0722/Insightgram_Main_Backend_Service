package com.Insightgram.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ProfilePhotoService {
	
	ContentByteAndType viewProfilePhoto(String uniqueUserIndetifier) throws IOException;
	
	byte[] addProfilePhoto(MultipartFile file) throws IllegalStateException, IOException ;
	
	byte[] changeProfilePhoto(MultipartFile file) throws IllegalStateException, IOException ;
	
	String removeProfilePhoto();
	
}
