package com.Insightgram.utils;

import org.apache.commons.validator.routines.UrlValidator;

public class Urls {
	public static boolean isUrlValid(String url) {
		UrlValidator urlValidator = new UrlValidator();
		return urlValidator.isValid(url);
	}
}
