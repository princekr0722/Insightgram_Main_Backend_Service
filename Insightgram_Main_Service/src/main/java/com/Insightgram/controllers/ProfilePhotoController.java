package com.Insightgram.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.services.ContentByteAndType;
import com.Insightgram.services.ProfilePhotoService;

@RestController
@RequestMapping("/main-app")
public class ProfilePhotoController {
	
		@Autowired
		private ProfilePhotoService profilePhotoService;
		
		@PostMapping("/user/profilePhoto")
		public ResponseEntity<?> addProfilePhoto(@RequestParam MultipartFile profilePhoto) throws IllegalStateException, IOException{
			byte[] profilePhotoBytes = profilePhotoService.addProfilePhoto(profilePhoto);
			return ResponseEntity.status(HttpStatus.OK)
					.contentType(MediaType.valueOf(profilePhoto.getContentType()))
					.body(profilePhotoBytes);
		}
		
		@PutMapping("user/profilePhoto")
		public ResponseEntity<?> changeProfilePhoto(@RequestParam MultipartFile profilePhoto) throws IllegalStateException, IOException{
			byte[] profilePhotoBytes = profilePhotoService.changeProfilePhoto(profilePhoto);
			return ResponseEntity.status(HttpStatus.OK)
					.contentType(MediaType.valueOf(profilePhoto.getContentType()))
					.body(profilePhotoBytes);
		}
		
		@GetMapping("/user/{uniqueIdentifier}/profilePhoto")
		public ResponseEntity<?> viewProfilePhoto(@PathVariable String uniqueIdentifier) throws IOException{
			ContentByteAndType contentByteAndType = profilePhotoService.viewProfilePhoto(uniqueIdentifier);
			return ResponseEntity.status(HttpStatus.OK)
					.contentType(MediaType.valueOf(contentByteAndType.getMediaType()))
					.body(contentByteAndType.getMediaBytes());
		}
		
		@DeleteMapping("user/profilePhoto")
		public ResponseEntity<String> deleteProfilePhoto(){
			return new ResponseEntity<String>(profilePhotoService.removeProfilePhoto(), HttpStatus.OK);
		}
}
