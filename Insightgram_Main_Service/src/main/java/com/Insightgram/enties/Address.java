package com.Insightgram.enties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	
	@NotBlank
	private String country;
	
	@NotBlank
	private String stateOrProvince;
	
	@NotBlank
	private String city;
	
	@Pattern(regexp = "\\d{6}", message = "Not a valid zip code.")
	private String zipCode;
	
	public Address(@NotBlank String country, @NotBlank String stateOrProvince,
			@Pattern(regexp = "\\d{6}", message = "Not a valid zip code.") String zipCode) {
		this.country = country;
		this.stateOrProvince = stateOrProvince;
		this.zipCode = zipCode;
	}


	@Override
	public String toString() {
		return "Address [country=" + country + ", stateOrProvince=" + stateOrProvince
				+ ", city=" + city + ", zipCode=" + zipCode + "]";
	} 
	
}
