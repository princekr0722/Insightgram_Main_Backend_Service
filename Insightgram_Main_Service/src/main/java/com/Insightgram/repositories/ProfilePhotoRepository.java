package com.Insightgram.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.Insightgram.enties.ProfilePhoto;

public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, Integer>{
	
	@Query("FROM ProfilePhoto WHERE Concat(user.userId,'') = :uniqueIdentifier OR Concat(profilePhotoId,'') = :uniqueIdentifier OR user.username = :uniqueIdentifier OR user.mobileNumber = :uniqueIdentifier")
	Optional<ProfilePhoto> getProfilePhoto(String uniqueIdentifier);
	
	@Modifying
	@Query("UPDATE ProfilePhoto SET imagePath = :newProfilePhotoPath WHERE user.userId = :uniqueIdentifier OR profilePhotoId = :uniqueIdentifier")
	int changeProfilePhoto(Integer uniqueIdentifier, String newProfilePhotoPath);
	
	@Modifying
	@Query("Delete From ProfilePhoto Where  user.userId = :uniqueIdentifier OR profilePhotoId = :uniqueIdentifier")
	int deleteProfilePhoto(Integer uniqueIdentifier);
}
