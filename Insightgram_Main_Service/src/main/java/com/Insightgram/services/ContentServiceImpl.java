package com.Insightgram.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.dto.ContentUrlAndType;
import com.Insightgram.dto.UploadedFileDetails;
import com.Insightgram.enties.Content;
import com.Insightgram.enties.Post;
import com.Insightgram.enties.User;
import com.Insightgram.enties.enums.AccountType;
import com.Insightgram.repositories.ContentRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentRepository contentRepository;
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	@Value("${static.folder.path}")
	private String staticFolderPath;

	@Value("${posts.static.folder.path}")
	private String postsStaticFolderPath;

	@Value("${images.posts.static.folder.path}")
	private String imagesPostsStaticFolderPath;

	@Value("${videos.posts.static.folder.path}")
	private String videosPostsStaticFolderPath;

	@Value("${others.posts.static.folder.path}")
	private String othersPostsStaticFolderPath;

	@Override
	public Content createContent(MultipartFile file, Post post) throws IOException {
		
		UploadedFileDetails uploadedFileDetails = cloudinaryService.uploadMedia(file);
		
		Content content = new Content();
		content.setContentPublicId(uploadedFileDetails.getPublicId());
		content.setContentType(uploadedFileDetails.getResourceType());
		content.setContentUrl(uploadedFileDetails.getUrl());
		content.setPost(post);

		return contentRepository.save(content);
	}

	private Content getContentById(Integer contentId) {
		Content content = contentRepository.findById(contentId)
				.orElseThrow(() -> new IllegalArgumentException("No content is availbale with id: " + contentId));
		return content;
	}

	@Override
	public ContentUrlAndType viewContent(Integer contentId) throws IOException {

		ContentUrlAndType contentUrlAndType = getContentFileUrl(contentId);

		return contentUrlAndType;
	}

	private ContentUrlAndType getContentFileUrl(Integer contentId) {
		Content content = getContentById(contentId);
		String contentUrl;

		String uniqueUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
		Post post = content.getPost();
		User postOwner = post.getUser();

		if (postOwner.getAccountType() == AccountType.PUBLIC || uniqueUserIdentifier.equals(postOwner.getUsername())
				|| uniqueUserIdentifier.equals(postOwner.getMobileNumber())) {
			contentUrl = content.getContentUrl();
		} else {

			boolean isfollow = false;
			for (User follower : postOwner.getFollowers()) {
				if (uniqueUserIdentifier.equals(follower.getUsername())
						|| uniqueUserIdentifier.equals(postOwner.getMobileNumber())) {
					isfollow = true;
					break;
				}
			}

			if (!isfollow)
				throw new IllegalArgumentException(
						"Content owner's account is private, and you're not a follower of the content owner.");
			else
				contentUrl = content.getContentUrl();
		}

		ContentUrlAndType res = new ContentUrlAndType(contentUrl, content.getContentType());
		return res;
	}

	@Override
	public boolean deleteContent(Content content) {
		String publicId = content.getContentPublicId();
		String resoureType = content.getContentType();
		
		return cloudinaryService.deleteMedia(publicId, resoureType);
	}

//	@Autowired
//	private ResourceLoader resourceLoader;

//	@Override
//	public Mono<Resource> streamLiveVideo(Integer contentId) {
//		String[] fileTypeAndPath = getContentFilePath(contentId);
//		if (!Content.isValidVideoFormat(fileTypeAndPath[0]))
//			throw new IllegalArgumentException("Content with Id: " + contentId + " is not a video.");
//		return Mono.fromSupplier(() -> resourceLoader.getResource("file:" + fileTypeAndPath[1]));
//	}
	
	@Override
	public ContentUrlAndType streamLiveVideo(Integer contentId) {
		ContentUrlAndType contentUrlAndType = getContentFileUrl(contentId);
		
		if (!contentUrlAndType.getMediaType().equals("video"))
			throw new IllegalArgumentException("Content with Id: " + contentId + " is not a video.");
		return contentUrlAndType;
	}
}
