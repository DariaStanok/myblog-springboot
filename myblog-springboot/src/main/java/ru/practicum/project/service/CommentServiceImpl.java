package ru.practicum.project.service;

import org.springframework.stereotype.Service;

import ru.practicum.project.dao.CommentRepository;

@Service
public class CommentServiceImpl  implements CommentService{

	private final CommentRepository commentRepository;
	
	public CommentServiceImpl(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	@Override
	public void addComment(Long postId, String text) {
		commentRepository.save(postId, text);
		
	}

	@Override
	public void editComment(Long postId, Long commentId, String text) {
		commentRepository.update(commentId, text);
		
	}

	@Override
	public void deleteComment(Long postId, Long commentId) {
		commentRepository.delete(commentId);
		
	}
}
