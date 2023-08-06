package com.Insightgram.entities.forms;

import com.Insightgram.enties.enums.Gender;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProfileForm {
	
	@Size(max = 150, message = "Field length must be at most 150 characters")
	private String bio;
	
	private String website;
	
	@NotNull
	private Gender gender;
	
}
