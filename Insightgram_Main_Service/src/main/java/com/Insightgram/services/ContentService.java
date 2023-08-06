package com.Insightgram.services;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.Insightgram.enties.Content;
import com.Insightgram.enties.Post;

import reactor.core.publisher.Mono;

public interface ContentService {

	ContentByteAndType viewContent(Integer contentId) throws IOException;

	Content createContent(MultipartFile file, Post post) throws IOException;

	public boolean deleteContent(Content content);
	
	public Mono<Resource> streamLiveVideo(Integer contentId);
}
