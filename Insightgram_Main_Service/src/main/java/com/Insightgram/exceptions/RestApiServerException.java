package com.Insightgram.exceptions;

import feign.Body;
import feign.Response;

public class RestApiServerException extends RuntimeException {
	public RestApiServerException(String requestUrl, Response.Body responseBody) {
		super(requestUrl+": "+responseBody);
	}
}
