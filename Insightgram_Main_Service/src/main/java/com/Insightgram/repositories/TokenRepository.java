package com.Insightgram.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.Insightgram.enties.Token;

public interface TokenRepository extends JpaRepository<Token, Integer>{
	
	@Query("Select token From Token Where user.userId = :userId AND expired = false AND revoked = false")
	List<String> findAllValidTokensByUser(Integer userId);
	
	Optional<Token> findByToken(String token);
	
	@Modifying
	@Query("Update Token SET revoked = true where token = :token")
	int revokeToken(String token);
	
	@Query("Select (expired=false AND revoked=false) From Token where token = :token")
	Boolean isTokenValid(String token);
}
