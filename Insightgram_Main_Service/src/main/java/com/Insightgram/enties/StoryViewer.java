package com.Insightgram.enties;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StoryViewer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne
	@JoinColumn(name = "story_id")
	private Story story;
	
	@ManyToOne
	@JoinColumn(name = "viewer_id")
	private User viewer;
	
	@JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
	private LocalDateTime viewDateTime = LocalDateTime.now();
	
	public StoryViewer(Story story, User viewer){
		this.story = story;
		this.viewer = viewer;
	}
	
}
