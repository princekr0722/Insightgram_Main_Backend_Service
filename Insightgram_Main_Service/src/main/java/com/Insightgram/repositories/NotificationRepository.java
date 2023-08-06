package com.Insightgram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.Insightgram.enties.Notication;

public interface NotificationRepository extends JpaRepository<Notication, Integer>, PagingAndSortingRepository<Notication, Integer>{

}
