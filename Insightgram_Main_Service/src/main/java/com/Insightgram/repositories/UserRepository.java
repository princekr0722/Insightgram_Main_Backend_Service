package com.Insightgram.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.Insightgram.dto.UserBasicInfo;
import com.Insightgram.dto.UserDto;
import com.Insightgram.dto.UserProfileDto;
import com.Insightgram.enties.User;
import com.Insightgram.enties.enums.AccountType;
import com.Insightgram.enties.enums.Gender;


public interface UserRepository extends JpaRepository<User, Integer>, PagingAndSortingRepository<User, Integer>{
	
	Optional<User> findByUsername(String username);
	
	Optional<User> findByMobileNumber(String mobileNumber);
	
	@Query("Select userId From User Where username = :usernameOrMobile OR mobileNumber = :usernameOrMobile")
	Optional<Integer> getUserIdByUsernameOrMobile(String usernameOrMobile);
	
	@Query("Select new com.Insightgram.dto.UserBasicInfo(user) From User as user Where user.username LIKE %:searchKeyword% OR Concat(user.firstName, ' ', user.lastName) LIKE %:searchKeyword%")
	Page<UserBasicInfo> searchUserPageWise(String searchKeyword, Pageable pageable);

	@Query("Select new com.Insightgram.dto.UserDto(user) From User as user Where user.userId = :userId")
	Optional<UserDto> getPublicProfileOfUser(Integer userId);
	
	@Query("Select new com.Insightgram.dto.UserProfileDto(user) From User user Where user.username = :userIdentifier OR user.mobileNumber = :userIdentifier")
	Optional<UserProfileDto> getMyProfile(String userIdentifier);
	
	boolean existsByUsername(String username);
	
	boolean existsByMobileNumber(String mobileNumber);
	
	@Query("Select accountType From User Where username = :userIdentifier OR mobileNumber = :userIdentifier")
	AccountType getAccountTypeOfUser(String userIdentifier);
	
	@Modifying
	@Query("Update User SET accountType = :accountType Where username = :userIdentifier OR mobileNumber = :userIdentifier")
	int changeAccountTypeOfUser(String userIdentifier, AccountType accountType);
	
	@Modifying
	@Query("Update User SET firstName = :firstName, lastName = :lastName, dob = :dob "
			+ "Where username = :userIdentifier OR mobileNumber = :userIdentifier")
	int changeBasicUserInfo(String firstName, String lastName, LocalDate dob, String userIdentifier);

	@Modifying
	@Query("Update User SET bio = :bio, website = :website, gender = :gender "
			+ "Where username = :userIdentifier OR mobileNumber = :userIdentifier")
	int editUserProfile(String bio, String website, Gender gender, String userIdentifier);

	@Query("SELECT new com.Insightgram.dto.UserBasicInfo(user) FROM User as user WHERE user.username = :username")
	Optional<UserBasicInfo> getUserBasicInfoByUsername(String username);
}
