package com.Insightgram.config.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomErrorResponse {
	private String errorMessage;
	private int statusCode;
	
	public CustomErrorResponse(String errorMessage, int statusCode) {
		this.errorMessage = errorMessage;
		this.statusCode = statusCode;
	}
}
