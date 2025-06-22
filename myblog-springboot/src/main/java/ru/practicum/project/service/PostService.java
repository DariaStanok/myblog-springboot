package ru.practicum.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ru.practicum.project.model.Paging;
import ru.practicum.project.model.Post;

@Service
public interface PostService {

	List<Post> findAll(String search, Paging paging);
	
	Post findById(Long id);
	
	Post save (Post post, MultipartFile image, String tags);
	
	Post update (Long id, Post updatePost, MultipartFile image, String tags);
	
	void deleteById (Long id);
	
	void like (Long id, boolean like);
	
}
