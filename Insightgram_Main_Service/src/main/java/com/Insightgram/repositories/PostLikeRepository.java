package com.Insightgram.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.Insightgram.dto.PostLikeDto;
import com.Insightgram.enties.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Integer>, PagingAndSortingRepository<PostLike, Integer>{
	
	@Query("select post.postId From PostLike where post.postId = :postId AND user.userId = :userId")
	Optional<PostLike> haveLiked(Integer postId, Integer userId);
	
	@Modifying
	@Query("Delete From PostLike Where post.postId = :postId AND user.userId = :userId")
	int unlikePost(Integer postId, Integer userId);
	
	@Query("Select Count(like) From PostLike as like Where post.postId = :postId")
	int likeCount(Integer postId);
	
	@Query("Select new com.Insightgram.dto.PostLikeDto(like) From PostLike as like Where like.post.postId = :postId")
	Page<PostLikeDto> getAllPostLikes(Integer postId, Pageable pageable);

	@Query("Select Count(like) From PostLike as like where like.post.postId=:postId AND like.user.userId = :userId")
	int havedUserLikedThePostAlready(Integer postId, Integer userId);
	
	@Modifying
	@Query("DELETE FROM PostLike WHERE post.postId = :postId")
	int deleteAllLikesOfPost(Integer postId);
	
}