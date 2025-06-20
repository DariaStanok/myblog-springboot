package ru.practicum.project.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository {

	List<String> loadTags(Long postId);

	void deleteTags(Long postId);

	void insertTags(Long postId, List<String> tags);
}
