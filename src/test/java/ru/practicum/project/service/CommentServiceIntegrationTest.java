package ru.practicum.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.practicum.project.dao.CommentRepository;
import ru.practicum.project.dao.PostRepository;
import ru.practicum.project.model.Comment;
import ru.practicum.project.model.Post;

@SpringBootTest
public class CommentServiceIntegrationTest {

	@Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private Long postId;

    @BeforeEach
    void setUp() {
        Post post = new Post();
        post.setTitle("Test post");
        post.setText("Post content");
        postId = postRepository.save(post);
    }
    
    @AfterEach
    void cleanup() {
        commentRepository.loadComments(postId).forEach(c -> commentRepository.delete(c.getId()));
        postRepository.deleteById(postId);
    }

    @Test
    void addSaveComment() {
        commentService.addComment(postId, "Integration comment");

        List<Comment> comments = commentRepository.loadComments(postId);
        assertFalse(comments.isEmpty());
        assertEquals("Integration comment", comments.get(0).getText());
    }

    @Test
    void editUpdateComment() {
        commentService.addComment(postId, "To be updated");
        Comment comment = commentRepository.loadComments(postId).get(0);

        commentService.editComment(postId, comment.getId(), "Updated comment");

        List<Comment> updated = commentRepository.loadComments(postId);
        assertEquals("Updated comment", updated.get(0).getText());
    }

    @Test
    void deleteComment() {
        commentService.addComment(postId, "To be deleted");
        Comment comment = commentRepository.loadComments(postId).get(0);

        commentService.deleteComment(postId, comment.getId());

        List<Comment> comments = commentRepository.loadComments(postId);
        assertTrue(comments.isEmpty());
    }
}



