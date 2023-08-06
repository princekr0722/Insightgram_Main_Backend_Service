package com.Insightgram.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

	private void validateProfilePhoto(MultipartFile file) {
		if(!ProfilePhoto.isValidImageFormat(file.getContentType())) 
			throw new IllegalArgumentException("Media type is not valid, Valid media type for profile photo: "
					+Arrays.toString(ProfilePhoto.SUPPORTED_IMAGE_FORMATS));
	}
	
	@Override
	public ContentByteAndType viewProfilePhoto(String  uniqueIndetifier) throws IOException {
		Optional<ProfilePhoto> opt = profilePhotoRepository.getProfilePhoto(uniqueIndetifier);
		if(opt.isEmpty()) throw new IllegalArgumentException("No profile photo exist with identifier: "+uniqueIndetifier);
		ProfilePhoto profilePhoto = opt.get();
		
		byte[] profilePhotoBytes = Files.readAllBytes(new File(profilePhoto.getImagePath()).toPath());
		return new ContentByteAndType(profilePhotoBytes, profilePhoto.getMediaType());
	}

	@Value("${profile.photos.static.folder.path}")
	private String profilePhotosStaticFolderPath;
	
	@Override
	public byte[] addProfilePhoto(MultipartFile file) throws IllegalStateException, IOException {
		validateProfilePhoto(file);
		
		Optional<ProfilePhoto> opt = profilePhotoRepository.getProfilePhoto(SecurityContextHolder.getContext().getAuthentication().getName()); 
		if(opt.isPresent()) throw new UserException("One user can have only one profile photo");
		
		Path path = Paths.get(profilePhotosStaticFolderPath).toAbsolutePath();
		File fileDirectory = path.toFile();
		
		String uniqueFileName = "/insightgram-PROFILE_PHOTO-"
				+ UUID.randomUUID().toString().substring(0, 20) + "-" + file.getOriginalFilename();
		
		String filePath = fileDirectory.getAbsolutePath() + uniqueFileName;

		file.transferTo(new File(filePath));
		ProfilePhoto profilePhoto = ProfilePhoto.builder()
				.imagePath(filePath)
				.user(userUtilServices.getCurrentUser())
				.mediaType(file.getContentType())
				.build();
		profilePhotoRepository.save(profilePhoto);
		
		return Files.readAllBytes(new File(filePath).toPath());
	}

	@Override
	public byte[] changeProfilePhoto(MultipartFile file) throws IllegalStateException, IOException {
		validateProfilePhoto(file);
		Optional<ProfilePhoto> opt = profilePhotoRepository.getProfilePhoto(SecurityContextHolder.getContext().getAuthentication().getName()); 
		if(opt.isEmpty()) throw new UserException("User have no profile photo");
		
		Integer userId = userUtilServices.getUserIdByUsernameOrMobile(SecurityContextHolder.getContext().getAuthentication().getName());
		
		Path path = Paths.get(profilePhotosStaticFolderPath).toAbsolutePath();
		File fileDirectory = path.toFile();
		
		String uniqueFileName = "/insightgram-PROFILE_PHOTO-"
				+ UUID.randomUUID().toString().substring(0, 20) + "-" + file.getOriginalFilename();
		
		String filePath = fileDirectory.getAbsolutePath() + uniqueFileName;
		
		file.transferTo(new File(filePath));
		
		profilePhotoRepository.changeProfilePhoto(userId, filePath);
		
		//deleting old profile from storage
		ProfilePhoto oldProfilePhoto = opt.get();
		new File(oldProfilePhoto.getImagePath()).delete();
		
		return Files.readAllBytes(new File(filePath).toPath());
	}
	
	@Override
	@Transactional
	public String removeProfilePhoto() {
		ProfilePhoto profilePhoto = deleteProfilePhoto();
		String profilePhotoPath = profilePhoto.getImagePath();
	 	
		new File(profilePhotoPath).delete();
		return "Profile photo has been removed";
	}
	
	private ProfilePhoto deleteProfilePhoto() {
		Optional<ProfilePhoto> opt = profilePhotoRepository.getProfilePhoto(SecurityContextHolder.getContext().getAuthentication().getName()); 
		if(opt.isEmpty()) throw new UserException("User have no profile photo");
		
		ProfilePhoto profilePhoto = opt.get();
		
		int deletedRows = profilePhotoRepository.deleteProfilePhoto(profilePhoto.getProfilePhotoId());
		if(deletedRows == 0) throw new UserException("Tried to delete the profile photo Id: "+profilePhoto.getProfilePhotoId()+" but not found.");
		
		return profilePhoto;
	}
}
