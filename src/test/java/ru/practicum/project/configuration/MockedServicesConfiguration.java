package ru.practicum.project.configuration;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.practicum.project.service.CommentService;
import ru.practicum.project.service.PostService;

@Configuration
public class MockedServicesConfiguration {

	@Bean
    PostService postService() {
        return mock(PostService.class);
    }

    @Bean
    CommentService commentService() {
        return mock(CommentService.class);
    }    
}
