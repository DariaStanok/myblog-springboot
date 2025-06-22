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
import ru.practicum.project.model.Comment;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { DataSourceConfiguration.class, JdbcNativeCommentRepository.class})
class JdbcNativeCommentRepositoryTest extends RepositoryTestBase {
	
	@Autowired
	JdbcNativeCommentRepository commentRepository;

	@BeforeEach
    void setup() {
        resetDatabase();
        insertPost1();
        insertComment1(); 
    }

    @Test
    void loadCommentsPostFromDb() {
        List<Comment> comments = commentRepository.loadComments(1L);
        assertEquals(1, comments.size());
        assertEquals("like!", comments.get(0).getText());
    }

    @Test
    void addCommentsPostFromDb() {
        commentRepository.save(1L, "like it");
        List<Comment> comments = commentRepository.loadComments(1L);
        assertEquals(2, comments.size());
        assertTrue(comments.stream().anyMatch(c -> c.getText().equals("like it")));
    }

    @Test
    void updateCommentPostFromDb() {
        commentRepository.update(1L, "Updated comment");
        List<Comment> comments = commentRepository.loadComments(1L);
        assertEquals("Updated comment", comments.get(0).getText());
    }

    @Test
    void deleteCommentPostFromDb() {
        commentRepository.delete(1L);
        List<Comment> comments = commentRepository.loadComments(1L);
        assertTrue(comments.isEmpty());
    }
}


