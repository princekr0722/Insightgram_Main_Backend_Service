package com.Insightgram.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.dto.ContentUrlAndType;
import com.Insightgram.dto.StoryDto;
import com.Insightgram.dto.UploadedFileDetails;
import com.Insightgram.dto.UserBasicInfo;
import com.Insightgram.enties.Story;
import com.Insightgram.enties.StoryViewer;
import com.Insightgram.enties.User;
import com.Insightgram.exceptions.StoryException;
import com.Insightgram.exceptions.UserException;
import com.Insightgram.models.PageOf;
import com.Insightgram.repositories.StoryRepository;
import com.Insightgram.repositories.StoryViewerRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class StoryServiceImpl implements StoryService {

	@Autowired
	private StoryRepository storyRepository;

	@Autowired
	private StoryViewerRepository storyViewerRepository;

	@Autowired
	private UserUtilServices userUtilServices;
	
	@Autowired 
	private CloudinaryService cloudinaryService;

	@Override
	public StoryDto postStory(MultipartFile file) throws IllegalStateException, IOException {
		if (!Story.isValidContentType(file.getContentType()))
			throw new IllegalArgumentException(
					"Media type is unsupported for story. Supported media types are: " + Story.getSupportedMediaType());

		UploadedFileDetails uploadedFileDetails = saveStoryMediaToFolder(file);

		User currentUser = userUtilServices.getCurrentUser();
		Story story = new Story(uploadedFileDetails, currentUser);
		Story newStory = storyRepository.save(story);

//		set view count here
		StoryDto storyDto = new StoryDto(newStory);

		// delete after 24 hours
		deleteStoryAfter24Hours(newStory);

		return storyDto;
	}

	private void deleteStoryAfter24Hours(Story story) {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		if (!storyRepository.existsById(story.getStoryId())) {
			throw new IllegalArgumentException(
					"At the time of deletion of story after 24 hours story with Id: " + story.getStoryId());
		}
		scheduler.schedule(() -> {

			deleteStory(story.getStoryId());

		}, 24, TimeUnit.HOURS);
	}

	@Value("${static.folder.path}")
	private String staticFolderPath;

	@Value("${stories.static.folder.path}")
	private String storiesStaticFolderPath;

	@Value("${images.stories.static.folder.path}")
	private String imagesStoriesStaticFolderPath;

	@Value("${videos.stories.static.folder.path}")
	private String videosStoriesStaticFolderPath;

	@Value("${others.stories.static.folder.path}")
	private String othersStoriesStaticFolderPath;

	private UploadedFileDetails saveStoryMediaToFolder(MultipartFile file) throws IllegalStateException, IOException {
		UploadedFileDetails uploadedFileDetails = cloudinaryService.uploadMedia(file);
		return uploadedFileDetails;
	}

	@Override
	public String deleteStory(Integer storyId) {
		String currentUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();

		int availiblity = storyRepository.isStoryAvailable(currentUserIdentifier, storyId);
		if (availiblity == 0)
			throw new StoryException("User have no story with Id: " + storyId);

		deleteStoryViews(storyId);

		String publicIdAndContentType = storyRepository.getPublicIdAndContentTypeOfStory(storyId)
				.orElseThrow(() -> new StoryException("No story found with the Id: " + storyId));
		storyRepository.deleteById(storyId);
		
		String[] publicIdAndContentTypeArr = publicIdAndContentType.split(",");
		
		String publicId = publicIdAndContentTypeArr[0];
		String contentType = publicIdAndContentTypeArr[1];
		
		cloudinaryService.deleteMedia(publicId, contentType);

		return "Story deleted Successful with Id: " + storyId;
	}

	@Override
	public ContentUrlAndType viewStory(Integer storyId) throws IOException {
		Story story = storyRepository.findById(storyId)
				.orElseThrow(() -> new StoryException("No story exists with Id: " + storyId));

		User storyOwner = story.getUser();
		User currentUser = userUtilServices.getCurrentUser();

		if (!userUtilServices.canInteractWithAnotherUser(currentUser.getUserId(), storyOwner))
			throw new UserException("Story owner's account is private and you're not a follower of post owner");

		String storyContentPath = story.getStoryContentURL();

		if (currentUser.getUserId() != storyOwner.getUserId()) {
			int viewerCount = storyViewerRepository.isAlreadyAViewer(story.getStoryId(), currentUser.getUserId());
			if (viewerCount == 0) {
				StoryViewer storyViewer = new StoryViewer(story, currentUser);
				storyViewerRepository.save(storyViewer);
			}
		}

		return new ContentUrlAndType(storyContentPath, story.getStoryContentType());
	}

	@Override
	public PageOf<UserBasicInfo> seeViewers(Integer storyId, Integer pageSize, Integer pageNumber) {
		if (pageNumber < 0)
			throw new IllegalArgumentException("Page index must not be less than or equals to zero");

		String currentUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
		int storyCount = storyRepository.isStoryAvailable(currentUserIdentifier, storyId);
		if (storyCount == 0)
			throw new StoryException("User have no story with Id: " + storyId);

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<UserBasicInfo> viewersPage = storyViewerRepository.seeViewers(storyId, pageable);

		return new PageOf<>(viewersPage);

	}

	@Override
	public List<StoryDto> getStoriesOfUser(Integer userId) {
		User user = userUtilServices.getUserById(userId);
		String currentUserItendifier = SecurityContextHolder.getContext().getAuthentication().getName();
		Integer currentUserId = userUtilServices.getUserIdByUsernameOrMobile(currentUserItendifier);
		if (!userUtilServices.canInteractWithAnotherUser(currentUserId, user))
			throw new UserException("Account is private and you're not a follower of this account");

		List<StoryDto> allStory = new ArrayList<>();
		user.getStories().forEach(s -> {
			allStory.add(new StoryDto(s));
		});

		return allStory;
	}

	@Override
	public List<UserBasicInfo> followingsWhoHaveStories() {
		String currentUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
		return storyRepository.followingsWhoHaveStories(currentUserIdentifier);
	}

	@Override
	public int cleanUpStoriesOlderThanADay() {
		List<Object[]> storyIdsOlderThan24hrs = storyRepository.get24HrsOldStoryIdsPublicIdAndContentType(LocalDateTime.now());

		storyIdsOlderThan24hrs.forEach(objArr -> {
			Integer storyId = (Integer) objArr[0];
			deleteStoryViews(storyId);

			String publicId = (String) objArr[1];
			String contentType = (String) objArr[2];
			cloudinaryService.deleteMedia(publicId, contentType);
		});
		int rowDeleted = storyRepository.delete24HrsOldStories(LocalDateTime.now());
		log.info(rowDeleted + " stories deleted which were older than 24 hours.");
		return rowDeleted;
	}

	private int deleteStoryViews(Integer storyId) {
		return storyViewerRepository.deleteViewersOfStory(storyId);
	}

}
