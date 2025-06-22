package ru.practicum.project.dao;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ru.practicum.project.configuration.DataSourceConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DataSourceConfiguration.class, JdbcNativeTagRepository.class})
class JdbcNativeTagRepositoryTest extends RepositoryTestBase{
	
	@Autowired
	JdbcNativeTagRepository tagRepository;

	 @BeforeEach
	    void setup() {
	        resetDatabase();
	        insertPost1();
	        insertTag1();
	    }

	    @Test
	    void loadTagsPostFromDb () {
	        List<String> tags = tagRepository.loadTags(1L);
	        assertEquals(1, tags.size());
	        assertEquals("tag1", tags.get(0));
	    }

	    @Test
	    void insertTagsPostFromDb() {
	        tagRepository.insertTags(1L, List.of("newtag", "like"));
	        List<String> tags = tagRepository.loadTags(1L);
	        assertEquals(3, tags.size());
	        assertTrue(tags.containsAll(List.of("tag1", "newtag", "like")));
	    }

	    @Test
	    void deleteTagsPostFromDb() {
	        tagRepository.deleteTags(1L);
	        List<String> tags = tagRepository.loadTags(1L);
	        assertTrue(tags.isEmpty());
	    }	
}
