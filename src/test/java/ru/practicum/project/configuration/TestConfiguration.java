package ru.practicum.project.configuration;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import ru.practicum.project.service.CommentService;
import ru.practicum.project.service.PostService;

@Configuration
public class TestConfiguration {

	@Bean
	@Primary
	public PostService postService() {
		return Mockito.mock(PostService.class);
	}

	@Bean
	@Primary
	public CommentService commentService() {
		return Mockito.mock(CommentService.class);
	}
}
