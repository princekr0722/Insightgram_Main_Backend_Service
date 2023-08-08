package com.Insightgram.config.storage;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfiguration {
	
	@Value("${cloudinary.cloud_name}")
	private String cloudinaryCloudName;
	
	@Value("${cloudinary.api_key}")
	private String cloudinaryApiKey;
	
	@Value("${cloudinary.api_secret}")
	private String cloudinaryApiSecret; 
	
	@Bean
	Cloudinary getCloudinary() {
		Map<?, ?> config = Map.of(
				"cloud_name", cloudinaryCloudName, 
				"api_key", cloudinaryApiKey, 
				"api_secret", cloudinaryApiSecret, 
				"secure", true);
		return new Cloudinary(config);
	}
	
}
