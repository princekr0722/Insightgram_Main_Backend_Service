package com.Insightgram.entities.forms;

import java.time.LocalDate;

import com.Insightgram.enties.Address;
import com.Insightgram.enties.enums.AccountType;
import com.Insightgram.enties.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationForm {
	
	@NotBlank
	private String username;
	
	@Pattern(regexp = "\\d{10}", message = "Not a valid number.")
	private String mobileNumber;

//	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^!])(?=\\S+$).{8,16}$", 
//	message = "Password must contain alphanumeric, at least one special character, at least one uppercase letter, at least one lowercase letter, at least one digit and must of 6-12 characters.")
	@NotBlank
//	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$", message = "Password must have 8 to 16 characters and include at least one number, one uppercase and lowercase letter, one symbol (@,#,$,%,^,*,+), and no spaces.")
	private String password;
	
	@NotNull
	private AccountType accountType = AccountType.PUBLIC;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
//	@JsonFormat(pattern = "dd-MM-yyyy")
	@Past(message = "Date of birth must be in past")
	private LocalDate dob;
	
	@NotNull
	private Gender gender;
	
	private Address address;
}
