package com.Insightgram.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.Insightgram.dto.PostCommentDto;
import com.Insightgram.enties.PostComment;

public interface PostCommentRepository extends JpaRepository<PostComment, Integer>, PagingAndSortingRepository<PostComment, Integer>{
	
	@Modifying
	@Query("Delete From PostComment where commentId = :commentId AND post.postId = :postId AND user.userId = :userId")
	int deleteComment(Integer commentId, Integer postId, Integer userId);
	
	@Query("Select Count(comment) From PostComment as comment Where comment.post.postId = :postId")
	int countCommentsOnPost(Integer postId);
	
	//TODO add page wise comment retrieval 
	@Query("Select new com.Insightgram.dto.PostCommentDto(comment) From PostComment as comment Where comment.post.postId = :postId")
	Page<PostCommentDto> getCommentsOfPost(Integer postId, Pageable pageable);
	
	@Modifying
	@Query("Delete From PostComment where commentId = :commentId AND post.postId = :postId")
	int deleteCommentByOwner(Integer commentId, Integer postId);
}
