package ru.practicum.project.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.practicum.project.dao.CommentRepository;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

	@Mock
	CommentRepository commentRepository;

	@InjectMocks
	CommentServiceImpl commentService;

	@Test
	void addComment_delegatesToRepository() {
		commentService.addComment(1L, "text");
		verify(commentRepository).save(1L, "text");
	}

	@Test
	void editComment_delegatesToRepository() {
		commentService.editComment(1L, 10L, "updated text");
		verify(commentRepository).update(10L, "updated text");
	}

	@Test
	void deleteComment_delegatesToRepository() {
		commentService.deleteComment(1L, 10L);
		verify(commentRepository).delete(10L);
	}
}
