package ru.practicum.project.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcNativeTagRepository implements TagRepository {

	private final JdbcTemplate jdbcTemplate;
	
	public JdbcNativeTagRepository (JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<String> loadTags(Long postId) {
        return jdbcTemplate.queryForList("select text from tags where post_id = ?", String.class, postId);
	}

	@Override
	public void deleteTags(Long postId) {
		jdbcTemplate.update("delete from tags where post_id = ?", postId);

	}

	@Override
	public void insertTags(Long postId, List<String> tags) {
		 tags.forEach(tag ->
	        jdbcTemplate.update("insert into tags (post_id, text) values (?, ?)", postId, tag)
	    );
	}
}
