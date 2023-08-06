package com.Insightgram.enties;

import com.Insightgram.enties.enums.NotificationStatus;
import com.Insightgram.enties.enums.NotificationTopic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notication {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int notificationId;
	
	@Column(nullable = false)
	private NotificationTopic topic;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationStatus status = NotificationStatus.UNSEEN;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String message;
//	private String token;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
