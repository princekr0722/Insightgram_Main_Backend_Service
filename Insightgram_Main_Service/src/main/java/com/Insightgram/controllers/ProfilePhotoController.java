package com.Insightgram.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.Insightgram.dto.ContentUrlAndType;
import com.Insightgram.services.ProfilePhotoService;

@RestController
@RequestMapping("/main-app")
public class ProfilePhotoController {
	
		@Autowired
		private ProfilePhotoService profilePhotoService;
		
		@PostMapping("/user/profilePhoto")
		public ResponseEntity<String> addProfilePhoto(@RequestParam MultipartFile profilePhoto) throws IllegalStateException, IOException{
			String profilePhotoUrl = profilePhotoService.addProfilePhoto(profilePhoto);
			return ResponseEntity.status(HttpStatus.OK)
					.body(profilePhotoUrl);
		}
		
		@PutMapping("user/profilePhoto")
		public ResponseEntity<String> changeProfilePhoto(@RequestParam MultipartFile profilePhoto) throws IllegalStateException, IOException{
			String profilePhotoUrl = profilePhotoService.changeProfilePhoto(profilePhoto);
			return ResponseEntity.status(HttpStatus.OK)
					.body(profilePhotoUrl);
		}
		
		@GetMapping("/user/{uniqueIdentifier}/profilePhoto")
		public ResponseEntity<String> viewProfilePhoto(@PathVariable String uniqueIdentifier) throws IOException{
			ContentUrlAndType contentUrlAndType = profilePhotoService.viewProfilePhoto(uniqueIdentifier);
			return ResponseEntity.status(HttpStatus.OK)
					.body(contentUrlAndType.getMediaUrl());
		}
		
		@DeleteMapping("user/profilePhoto")
		public ResponseEntity<String> deleteProfilePhoto(){
			return new ResponseEntity<String>(profilePhotoService.removeProfilePhoto(), HttpStatus.OK);
		}
}
