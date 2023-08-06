package com.Insightgram.exceptions;

import feign.Response;

public class RestApiClientException extends RuntimeException {
	public RestApiClientException(String requestUrl, Response.Body responseBody) {
		super(requestUrl+": "+responseBody);
	}
}
