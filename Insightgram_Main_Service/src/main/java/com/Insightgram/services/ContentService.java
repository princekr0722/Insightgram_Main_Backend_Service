package com.Insightgram.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.dto.ContentUrlAndType;
import com.Insightgram.enties.Content;
import com.Insightgram.enties.Post;

public interface ContentService {

	ContentUrlAndType viewContent(Integer contentId) throws IOException;

	Content createContent(MultipartFile file, Post post) throws IOException;

	public boolean deleteContent(Content content);
	
	public ContentUrlAndType streamLiveVideo(Integer contentId);
}
