package com.Insightgram.enties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.Insightgram.enties.enums.AccountType;
import com.Insightgram.enties.enums.Gender;
import com.Insightgram.enties.enums.UserRole;
import com.Insightgram.entities.forms.UserCreationForm;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userId;
	
	@Column(unique = true, nullable = false)
	private String username;
	
	@Column(unique = true, nullable = false)
	private String mobileNumber;
	
	@Column(nullable = false)
	@JsonProperty(access = Access.WRITE_ONLY)
//	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$", message = "Password must have 8 to 16 characters and include at least one number, one uppercase and lowercase letter, one symbol (@,#,$,%,^,*,+), and no spaces.")
	private String password;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
	private ProfilePhoto profilePhoto;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole userRole = UserRole.ROLE_CUSTOMER;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AccountType accountType = AccountType.PUBLIC;
	
	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;
	
	@Column(nullable = false)
	private LocalDate dob;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Column(nullable = false)
	@JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
	private LocalDateTime enteredDate = LocalDateTime.now(); 
	
	
	@Embedded
	private Address address;
	
	@Column(length = 150)
	private String bio;
	private String website;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@JsonIgnore
	private Set<Story> stories = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@JsonIgnore
	private Set<Post> posts = new HashSet<>();
	
	@ManyToMany
    @JoinTable(
        name = "user_follows",
        joinColumns = @JoinColumn(name = "follower_id"),
        inverseJoinColumns = @JoinColumn(name = "followee_id")
    )
	@JsonIgnore
	private Set<User> following = new HashSet<>();

	@ManyToMany(mappedBy = "following")
	@JsonIgnore
	private Set<User> followers = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@JsonIgnore
	private Set<Notication> notifications = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@JsonIgnore
	private Set<Token> token = new HashSet<>();
	
	public User(String username, String mobileNumber, String password, AccountType accountType, String firstName,
			String lastName, LocalDate dob, Gender gender, Address address, String bio, String website) {
		this.username = username;
		this.mobileNumber = mobileNumber;
		this.password = password;
		this.accountType = accountType;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.gender = gender;
		this.address = address;
		this.bio = bio;
		this.website = website;
	}
	
	public User(UserCreationForm form) {
		this.username = form.getUsername();
		this.mobileNumber = form.getMobileNumber();
		this.password = form.getPassword();
		this.accountType = form.getAccountType();
		this.firstName = form.getFirstName();
		this.lastName = form.getLastName();
		this.dob = form.getDob();
		this.gender = form.getGender();
		this.address = form.getAddress();
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", mobileNumber=" + mobileNumber + ", password="
				+ password + ", userRole=" + userRole + ", accountType=" + accountType + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", dob=" + dob + ", gender=" + gender + ", enteredDate=" + enteredDate
				+ ", address=" + address + ", bio=" + bio + ", website=" + website + "]";
	}

	
}
