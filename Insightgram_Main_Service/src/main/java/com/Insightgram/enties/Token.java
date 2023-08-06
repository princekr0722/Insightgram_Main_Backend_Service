package com.Insightgram.enties;

import com.Insightgram.enties.enums.TokenType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
	
	@Id
	@GeneratedValue
	private Integer tokenId;
	
	@Column(nullable = false, unique = true, length = 10000)
	private String token;
	
	@Column(nullable = false)
	private TokenType tokenType;
	
	@Column(nullable = false)
	private Boolean expired;
	
	@Column(nullable = false)
	private Boolean revoked;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
