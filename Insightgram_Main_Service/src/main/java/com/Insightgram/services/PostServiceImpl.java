package com.Insightgram.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;

import com.Insightgram.dto.PostDto;
import com.Insightgram.enties.Content;
import com.Insightgram.enties.Post;
import com.Insightgram.enties.User;
import com.Insightgram.enties.enums.AccountType;
import com.Insightgram.enties.enums.PostType;
import com.Insightgram.entities.forms.PostCreationForm;
import com.Insightgram.exceptions.UserException;
import com.Insightgram.models.PageOf;
import com.Insightgram.repositories.PostLikeRepository;
import com.Insightgram.repositories.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserUtilServices userUtilServices;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private PostLikeRepository postLikeRepository;
	
	@Autowired
	private PostLikeService postLikeService;
	
	@Autowired
	private PostCommentService postCommentService;
	
	@Autowired
	private PostUtilServices postUtilServices;
	
	private Post getPostByForm(PostCreationForm postCreationForm, User user) {
		Post post = new Post();
		post.setPostType(postCreationForm.getPostType());
		post.setCaption(postCreationForm.getCaption());
		post.setPostDateTime(LocalDateTime.now());
		post.setUser(user);
		return post;
	}
	
	@Override
	@Transactional
	public PostDto createNewPost(PostCreationForm postCreationForm) throws IOException {
		
		User user = userUtilServices.getCurrentUser();
		
		Post post = getPostByForm(postCreationForm, user);
		
		Post newPost = postRepository.save(post);
		
		if(post.getPostType() == PostType.REEL) {
			if(postCreationForm.getContent().size()!=1)
				throw new IllegalArgumentException("Reel does not support multiple media.");
			
			MultipartFile file = new ArrayList<>(postCreationForm.getContent()).get(0);
			
			if(!(Content.isValidVideoFormat(file.getContentType())))
				throw new UnsupportedMediaTypeException("Unsupported media type for a reel.");

			Content content = contentService.createContent(file, newPost);
			newPost.addContent(content);
		}else {
			
			List<MultipartFile> files = new ArrayList<>(postCreationForm.getContent());
			
			files.forEach(file -> {
				if(!(Content.isValidContentType(file.getContentType()))
				) throw new UnsupportedMediaTypeException(file.getOriginalFilename()+" is Unsupported media type for a post.");
			});
			
			for(MultipartFile file: files) {
				Content content = contentService.createContent(file, newPost);
				newPost.addContent(content);
			}
		}
		return new PostDto(newPost);
	}
	
	@Override
	public PostDto viewPostById(Integer postId) throws IllegalAccessException {
		Post post = postUtilServices.getPostById(postId);
		
		User postOwner = post.getUser();
		String uniqueUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if(postOwner.getAccountType() != AccountType.PRIVATE) {	
			return getPostDto(post);
		}else {
			boolean isfollow = false;
			for(User follower: postOwner.getFollowers()) {
				if(uniqueUserIdentifier.equals(follower.getUsername()) || uniqueUserIdentifier.equals(postOwner.getMobileNumber())) {
					isfollow = true;
					break;
				}
			}
			
			if(!isfollow) throw new IllegalArgumentException("Post owner's account is private, and you're not a follower of the post owner.");
			else {
				return getPostDto(post);
			}
		}
	}
	
	private PostDto getPostDto(Post post) {
		PostDto postDto = new PostDto(post);
		postDto.setNoOfLikes(postLikeService.getLikeCount(post.getPostId()));
		postDto.setNoOfComments(postCommentService.countCommentsOnPost(post.getPostId()));
		return postDto;
	}

	@Override
	@Transactional
	public Boolean deletePostById(Integer postId) throws IllegalAccessException {
		Post post = postUtilServices.getPostById(postId);
		
		String uniqueUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if(!(post.getUser().getUsername().equals(uniqueUserIdentifier) || post.getUser().getMobileNumber().equals(uniqueUserIdentifier)))
			throw new IllegalAccessException("User does not own the post with ID: "+postId+". Therefore user can't delete it.");
		else {
			postLikeRepository.deleteAllLikesOfPost(postId);
			postRepository.deleteById(postId);
			
			post.getContent().forEach(c->{
				contentService.deleteContent(c);
			});
			return true;
		}
		
	}
	
	@Override
	public Boolean editPostCaptionById(Integer postId, String caption) throws IllegalAccessException {
		Post post = postUtilServices.getPostById(postId);
		
		String uniqueUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();

		if(!(post.getUser().getUsername().equals(uniqueUserIdentifier) || post.getUser().getMobileNumber().equals(uniqueUserIdentifier)))
			throw new IllegalAccessException("User does not own the post with ID: "+postId+". Therefore user can't edit it.");
		
		else {
			post.setCaption(caption);
			
			getPostDto(postRepository.save(post));
			return true; 
		}
	}

	@Override
	public PageOf<PostDto> getNewsFeedContent(Integer pageSize, Integer pageNumber) {
		if(pageNumber<0) throw new IllegalArgumentException("Page index must not be less than or equals to zero");
		String currentUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		PageOf<PostDto> pageOfPostDtos = new PageOf<>(postRepository.getNewFeedFolloweesPosts(currentUserIdentifier, pageable));
		pageOfPostDtos.getPageContent().forEach(pd->{
			Integer postLikeCount = postLikeService.getLikeCount(pd.getPostId());
			pd.setNoOfLikes(postLikeCount);
			
			Integer postCommentCount = postCommentService.countCommentsOnPost(pd.getPostId());
			pd.setNoOfComments(postCommentCount);
		});
		return pageOfPostDtos;
	}
	
	@Override
	public PageOf<PostDto> getPostsOfUser(Integer userId, Integer pageSize, Integer pageNumber) {
		if(pageNumber<0) throw new IllegalArgumentException("Page index must not be less than or equals to zero");
		User user = userUtilServices.getUserById(userId);
		Integer currentUserId = userUtilServices.getUserIdByUsernameOrMobile(SecurityContextHolder.getContext().getAuthentication().getName());

		if(!userUtilServices.canInteractWithAnotherUser(currentUserId, user)) {
			throw new UserException("Account is private and you're not a follower of this account.");
		}
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<PostDto> posts = postRepository.getPostsOfUser(userId, pageable);
		posts.getContent().forEach(postDto->{
			postDto.setNoOfLikes(postLikeService.getLikeCount(postDto.getPostId()));
			postDto.setNoOfComments(postCommentService.countCommentsOnPost(postDto.getPostId()));
		});
		return new PageOf<>(posts);
	}
}
