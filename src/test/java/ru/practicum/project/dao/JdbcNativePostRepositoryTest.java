package ru.practicum.project.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;

import ru.practicum.project.model.Post;

@DataJdbcTest
@Import(JdbcNativePostRepository.class)

class JdbcNativePostRepositoryTest extends RepositoryTestBase {
	
	@Autowired JdbcNativePostRepository postRepository;
    

	@BeforeEach
	void setup() {
		resetDatabase();
        insertPost1();
	}

	@Test
    void findByIdPostFromDb() {
        Post post = postRepository.findById(1L);
        assertNotNull(post);
        assertEquals("Title1", post.getTitle());
    }
	
	@Test
	void findAllPostsFromDb() {
		jdbcTemplate.execute("insert into posts (id, title, text, image_path, likes_count) values(2, 'Title2', 'text2', 'img2.jpg', 0)");
	    List<Post> posts = postRepository.findAll();
	    assertEquals(2, posts.size());
	    assertEquals("Title1", posts.get(0).getTitle());
	    assertEquals("Title2", posts.get(1).getTitle());
	}
	
	@Test
	void findByTagsPostFromDb() { 
		insertTag1();  
	    List<Post> result = postRepository.findByTag("tag1"); 
	    assertEquals(1, result.size());
	    assertEquals("Title1", result.get(0).getTitle()); 
	}

	@Test
	void savePostFromDb() {
		Post toSave = new Post(null, "Title text", "Content text", "pic.jpg", 0);
		Long id = postRepository.save(toSave);
		assertNotNull(id);
		assertEquals(id, toSave.getId());
		Post fromDb = postRepository.findById(id);
		assertEquals("Title text", fromDb.getTitle());
		assertEquals("Content text", fromDb.getText());
		assertEquals("pic.jpg", fromDb.getImagePath());
		assertEquals(0, fromDb.getLikesCount());
	}

	@Test
	void updatePostFromDb() {
		Post updated = new Post(1L, "Updated title", "Updated text", "new.jpg", 6);
		postRepository.update(updated);
		Post fromDb = postRepository.findById(1L);
		assertEquals("Updated title", fromDb.getTitle());
		assertEquals("Updated text", fromDb.getText());
		assertEquals("new.jpg", fromDb.getImagePath());
		assertEquals(6, fromDb.getLikesCount());
	}

	@Test
	void deleteByIdPostFromDb() {
		Post existing = postRepository.findById(1L);
		assertEquals("Title1", existing.getTitle());
		postRepository.deleteById(1L);
		assertThrows(EmptyResultDataAccessException.class, () -> postRepository.findById(1L));
	}

	@Test
	void updateLikesPostFromDb() {
		postRepository.updateLikes(1L, 3);
		postRepository.updateLikes(1L, -4);
		Post post = postRepository.findById(1L);
		assertEquals(4, post.getLikesCount());
	}
}
