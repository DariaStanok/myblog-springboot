package ru.practicum.project.service;

import org.springframework.stereotype.Service;

@Service
public interface CommentService {

	void addComment (Long postId, String text);
	
	void editComment (Long postId, Long commentId, String text);
	
	void deleteComment (Long postId, Long commentId);
}
