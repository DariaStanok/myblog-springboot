package ru.practicum.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import ru.practicum.project.model.Post;

@SpringBootTest
public class PostServiceIntegrationTest {
	
	@Autowired
	PostService postService;
	
	@TempDir
	static Path tempDir;
	
	@DynamicPropertySource
	static void overrideUploadDir(DynamicPropertyRegistry registry) {
	    registry.add("upload.dir", () -> tempDir.toString()); 
	}
	
	@Test
	void saveAndFindPost () throws Exception {
	    Post post = new Post();
	    post.setTitle("Integration title");
	    post.setText("Integration content");

	    MockMultipartFile image = new MockMultipartFile(
	        "image", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});

	    Post saved = postService.save(post, image, "tag7,tag8");

	    assertNotNull(saved.getId());
	    assertTrue(saved.getImagePath().endsWith(".jpg"));
	    assertEquals(List.of("tag7", "tag8"), saved.getTags());

	    Path savedFile = tempDir.resolve(
	        Path.of(saved.getImagePath()).getFileName());
	    assertTrue(Files.exists(savedFile));
	    
	    Post fromDb = postService.findById(saved.getId());
	    assertEquals("Integration title", fromDb.getTitle());
	    assertEquals(List.of("tag7", "tag8"), fromDb.getTags());
	}

}


