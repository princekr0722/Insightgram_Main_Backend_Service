package com.Insightgram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.Insightgram.services.StoryService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
@OpenAPIDefinition(servers = { @Server(url = "/", description = "Default Server URL") })
@EnableFeignClients
@EnableDiscoveryClient
public class InsightgramApplication {

	@Autowired
	private StoryService storyService;

	@PostConstruct
	private void cleanUpStoriesOlderThanADay() {
		storyService.cleanUpStoriesOlderThanADay();
	}

	public static void main(String[] args) {
		SpringApplication.run(InsightgramApplication.class, args);
	}

}
