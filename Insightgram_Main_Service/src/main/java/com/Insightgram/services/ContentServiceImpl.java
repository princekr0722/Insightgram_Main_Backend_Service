package com.Insightgram.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.enties.Content;
import com.Insightgram.enties.Post;
import com.Insightgram.enties.User;
import com.Insightgram.enties.enums.AccountType;
import com.Insightgram.repositories.ContentRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentRepository contentRepository;
	
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
		
		String uniqueFileName = "/insightgram-" + post.getPostType() + "-"
				+ UUID.randomUUID().toString().substring(0, 20) + "-" + file.getOriginalFilename();
		
		String basePath;
		String filePath;
		if(file.getContentType().split("/")[0].equals("video")) {
			
			basePath = videosPostsStaticFolderPath;
			
		}else if(file.getContentType().split("/")[0].equals("image")){
			
			basePath = imagesPostsStaticFolderPath;
			
		}else {
			basePath = othersPostsStaticFolderPath;
		}
		
		Path path = Paths.get(basePath).toAbsolutePath();
		File fileDirectory = path.toFile();

		filePath = fileDirectory.getAbsolutePath() + uniqueFileName;
		
		Content content = new Content();
		content.setName(uniqueFileName);
		content.setContentType(file.getContentType());
		content.setContentPath(filePath);
		content.setPost(post);

		file.transferTo(new File(filePath));

		return contentRepository.save(content);
	}

	private Content getContentById(Integer contentId) {
		Content content = contentRepository.findById(contentId)
				.orElseThrow(() -> new IllegalArgumentException("No content is availbale with id: " + contentId));
		return content;
	}

	@Override
	public ContentByteAndType viewContent(Integer contentId) throws IOException {

		String[] fileTypeAndPath = getContentFilePath(contentId);

		byte[] media = Files.readAllBytes(new File(fileTypeAndPath[1]).toPath());

		return new ContentByteAndType(media, fileTypeAndPath[0]);
	}

	private String[] getContentFilePath(Integer contentId) {
		Content content = getContentById(contentId);
		String filePath;

		String uniqueUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
		Post post = content.getPost();
		User postOwner = post.getUser();

		if (postOwner.getAccountType() == AccountType.PUBLIC || uniqueUserIdentifier.equals(postOwner.getUsername())
				|| uniqueUserIdentifier.equals(postOwner.getMobileNumber())) {
			filePath = content.getContentPath();
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
				filePath = content.getContentPath();
		}

		String[] res = new String[] { content.getContentType(), filePath };
		return res;
	}

	@Override
	public boolean deleteContent(Content content) {
		File file = new File(content.getContentPath());
		return file.delete();
	}

	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public Mono<Resource> streamLiveVideo(Integer contentId) {
		String[] fileTypeAndPath = getContentFilePath(contentId);
		if (!Content.isValidVideoFormat(fileTypeAndPath[0]))
			throw new IllegalArgumentException("Content with Id: " + contentId + " is not a video.");
		return Mono.fromSupplier(() -> resourceLoader.getResource("file:" + fileTypeAndPath[1]));
	}
}
