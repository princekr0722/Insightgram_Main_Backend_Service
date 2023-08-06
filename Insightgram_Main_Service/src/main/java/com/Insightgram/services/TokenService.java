package com.Insightgram.services;

import java.util.concurrent.TimeUnit;

public interface TokenService {
	Boolean isTokenValid(String token);
	boolean revokeToken(String token);
	void scheduleRevokeToken(Integer tokenId, long delay, TimeUnit timeUnit);
}
