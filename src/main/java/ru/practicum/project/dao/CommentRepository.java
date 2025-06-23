package ru.practicum.project.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import ru.practicum.project.model.Comment;

@Repository
public interface CommentRepository {

	List<Comment> loadComments(Long postId);
	
    void save(Long postId, String text);
    
    void update(Long commentId, String text);
    
    void delete(Long commentId);
	
}
