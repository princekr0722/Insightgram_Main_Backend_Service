package com.Insightgram.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.dto.ContentUrlAndType;
import com.Insightgram.dto.UploadedFileDetails;
import com.Insightgram.enties.ProfilePhoto;
import com.Insightgram.exceptions.UserException;
import com.Insightgram.repositories.ProfilePhotoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProfilePhotoServiceImpl implements ProfilePhotoService{

	@Autowired
	private ProfilePhotoRepository profilePhotoRepository;
	
	@Autowired
	private UserUtilServices userUtilServices;
	
	@Autowired
	private CloudinaryService cloudinaryService;

	private void validateProfilePhoto(MultipartFile file) {
		if(!ProfilePhoto.isValidImageFormat(file.getContentType())) 
			throw new IllegalArgumentException("Media type is not valid, Valid media type for profile photo: "
					+Arrays.toString(ProfilePhoto.SUPPORTED_IMAGE_FORMATS));
	}
	
	@Override
	public ContentUrlAndType viewProfilePhoto(String  uniqueIndetifier) throws IOException {
		String profilePhotoUrlAndType = profilePhotoRepository.getProfilePhotoUrlAndType(uniqueIndetifier)
				.orElseThrow(()->new IllegalArgumentException("No profile photo exist with identifier: "+uniqueIndetifier));
		
		String[] profilePhotoUrlAndTypeArr = profilePhotoUrlAndType.split(",");
		return new ContentUrlAndType(profilePhotoUrlAndTypeArr[0], profilePhotoUrlAndTypeArr[1]);
	}

	@Value("${profile.photos.static.folder.path}")
	private String profilePhotosStaticFolderPath;
	
	@Override
	public String addProfilePhoto(MultipartFile file) throws IllegalStateException, IOException {
		validateProfilePhoto(file);
		
		Optional<ProfilePhoto> opt = profilePhotoRepository.getProfilePhoto(SecurityContextHolder.getContext().getAuthentication().getName()); 
		if(opt.isPresent()) throw new UserException("One user can have only one profile photo");
		
		UploadedFileDetails uploadedFileDetails = cloudinaryService.uploadMedia(file);
		
		
		ProfilePhoto profilePhoto = ProfilePhoto.builder()
				.imagePublicId(uploadedFileDetails.getPublicId())
				.imageUrl(uploadedFileDetails.getUrl())
				.mediaType(uploadedFileDetails.getResourceType())
				.user(userUtilServices.getCurrentUser())
				.build();
		profilePhotoRepository.save(profilePhoto);
		
		return uploadedFileDetails.getUrl();
	}

	@Override
	public String changeProfilePhoto(MultipartFile file) throws IllegalStateException, IOException {
		validateProfilePhoto(file);
		Optional<ProfilePhoto> opt = profilePhotoRepository.getProfilePhoto(SecurityContextHolder.getContext().getAuthentication().getName()); 
		if(opt.isEmpty()) throw new UserException("User have no profile photo");
		
		Integer userId = userUtilServices.getUserIdByUsernameOrMobile(SecurityContextHolder.getContext().getAuthentication().getName());
		
		UploadedFileDetails uploadedFileDetails = cloudinaryService.uploadMedia(file);
		
		profilePhotoRepository.changeProfilePhoto(userId, uploadedFileDetails.getUrl());
		
		//deleting old profile from storage
		ProfilePhoto oldProfilePhoto = opt.get();
		cloudinaryService.deleteMedia(oldProfilePhoto.getImagePublicId(), oldProfilePhoto.getMediaType());
		
		return uploadedFileDetails.getUrl();
	}
	
	@Override
	@Transactional
	public String removeProfilePhoto() {
		deleteProfilePhoto();
		return "Profile photo has been removed";
	}
	
	private ProfilePhoto deleteProfilePhoto() {
		Optional<ProfilePhoto> opt = profilePhotoRepository.getProfilePhoto(SecurityContextHolder.getContext().getAuthentication().getName()); 
		if(opt.isEmpty()) throw new UserException("User have no profile photo");
		
		ProfilePhoto profilePhoto = opt.get();
		cloudinaryService.deleteMedia(profilePhoto.getImagePublicId(), profilePhoto.getMediaType());
		
		int deletedRows = profilePhotoRepository.deleteProfilePhoto(profilePhoto.getProfilePhotoId());
		if(deletedRows == 0) throw new UserException("Tried to delete the profile photo Id: "+profilePhoto.getProfilePhotoId()+" but not found.");
		
		return profilePhoto;
	}
}
