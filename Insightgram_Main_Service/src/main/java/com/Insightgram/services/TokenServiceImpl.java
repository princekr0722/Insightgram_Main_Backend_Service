package com.Insightgram.services;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Insightgram.repositories.TokenRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

	@Autowired
	private TokenRepository tokenRepository;
	
	@Override
	public Boolean isTokenValid(String token) {
		Boolean isValid = tokenRepository.isTokenValid(token);
		if(isValid == null) isValid = false;;
		return isValid;
	}
	
	@Override
	@Transactional
	public boolean revokeToken(String token) {
		int rowEdited = tokenRepository.revokeToken(token);
		return rowEdited != 0;
	}

	@Override
	@Transactional
	public void scheduleRevokeToken(Integer tokenId, long time, TimeUnit timeUnit) {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.schedule(()->{
			try {
				tokenRepository.deleteById(tokenId);
			} catch (Exception e) {
				 System.out.println(e);
			}
		}, time, timeUnit);
	}
}
