package com.Insightgram.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.Insightgram.dto.PostDto;
import com.Insightgram.enties.Post;

public interface PostRepository extends JpaRepository<Post, Integer>, PagingAndSortingRepository<Post, Integer>{
	@Query("Select new com.Insightgram.dto.PostDto(p) From User u Inner Join u.following uf "
			+ "Inner Join uf.posts p Where (u.username = :uniqueIdentifier OR u.mobileNumber = :uniqueIdentifier) "
			+ "And TIMESTAMPDIFF(HOUR, p.postDateTime, CURRENT_TIMESTAMP) <= 35 "
			+ "Order By p.postDateTime DESC")
	Page<PostDto> getNewFeedFolloweesPosts(String uniqueIdentifier, Pageable pageable);
	
	@Query("Select new com.Insightgram.dto.PostDto(post) From Post post "
			+ "Where post.user.userId = :userId Order By post.postDateTime DESC")
	Page<PostDto> getPostsOfUser(Integer userId, Pageable pageable);

	@Query("SELECT user.userId FROM Post WHERE postId = :postId")
	Integer getPostOwnerId(Integer postId);
}
