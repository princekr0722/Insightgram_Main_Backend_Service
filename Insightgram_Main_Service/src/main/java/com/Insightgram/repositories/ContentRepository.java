package com.Insightgram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.Insightgram.enties.Content;

public interface ContentRepository extends JpaRepository<Content, Integer>, PagingAndSortingRepository<Content, Integer>{
	
}
