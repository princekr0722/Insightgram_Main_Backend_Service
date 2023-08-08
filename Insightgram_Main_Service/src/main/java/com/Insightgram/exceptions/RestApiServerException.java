package com.Insightgram.exceptions;

import feign.Response;

public class RestApiServerException extends RuntimeException {
	public RestApiServerException(String requestUrl, Response.Body responseBody) {
		super(requestUrl+": "+responseBody);
	}
}
