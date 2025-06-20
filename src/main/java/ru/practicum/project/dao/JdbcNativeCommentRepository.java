package ru.practicum.project.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ru.practicum.project.model.Comment;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {

	private final JdbcTemplate jdbcTemplate;
	
	public JdbcNativeCommentRepository (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Comment> loadComments(Long postId) {
		return jdbcTemplate.query(
				"select id, text from comments where post_id = ? order by id", 
				(rs, rowNum) -> new Comment(
					rs.getLong("id"),
					rs.getString("text")
				),
				postId
			);
	}
	
	@Override
	public void save(Long postId, String text) {
	   jdbcTemplate.update("insert into comments (post_id, text) values (?, ?)", postId, text);
	}

	@Override
	public void update(Long commentId, String text) {
        jdbcTemplate.update("update comments set text = ? where id = ?", text, commentId);
	}
	
	@Override
	public void delete(Long commentId) {
	    jdbcTemplate.update("delete from comments where id = ?", commentId);
	}
}
