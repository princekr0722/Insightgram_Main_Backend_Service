package com.Insightgram.entities.forms;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalInfoForm {
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	@JsonFormat(pattern = "dd-MM-yyyy")
	@Past(message = "Date of birth must be in past")
	private LocalDate dob;
	
}
