package com.Insightgram.services.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.dto.UploadedFileDetails;
import com.Insightgram.services.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

	@Autowired
	private Cloudinary cloudinary;

	@Override
	public UploadedFileDetails uploadMedia(MultipartFile file) {

		try {
			Map<?, ?> uploadDetails = null;
			
			if(file.getContentType().split("/")[0].equals("video")) {
				uploadDetails = cloudinary.uploader().uploadLarge(file.getBytes(), ObjectUtils.asMap(
	                    "resource_type", "video"
	            ));	
			} else {
				uploadDetails = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
			}
			
			UploadedFileDetails uploadedFileDetails = UploadedFileDetails.builder()
					.publicId((String)uploadDetails.get("public_id"))
					.url((String)uploadDetails.get("secure_url"))
					.resourceType((String)uploadDetails.get("resource_type"))
					.build();
			return uploadedFileDetails;
			
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}

	}

	@Override
	public Boolean deleteMedia(String publicId, String resourceType) {
		try {
			Boolean isDeleted = false;
			Map<?, ?> res = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
					"resource_type", resourceType
			));
			String result = (String)res.get("result");
			
			if(result.toLowerCase().equals("ok")) {
				isDeleted = true;
			} else {
				isDeleted = false;
			}
			return isDeleted;
		} catch (IOException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}
}
