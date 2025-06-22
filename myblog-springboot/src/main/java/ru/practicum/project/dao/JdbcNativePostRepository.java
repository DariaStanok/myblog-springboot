package ru.practicum.project.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import ru.practicum.project.model.Post;

@Repository
public class JdbcNativePostRepository implements PostRepository  {

	private final JdbcTemplate jdbcTemplate;
	
	public JdbcNativePostRepository (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<Post> findAll() {
		return jdbcTemplate.query(
				"select id, title, text, image_path, likes_count from posts",
				(rs,rowNum)->new Post (
				rs.getLong("id"),
				rs.getString("title"),
				rs.getString("text"),
				rs.getString("image_path"),
				rs.getInt("likes_count")
				));
	} 
	
	@Override
	public List<Post> findByTag(String tag) {
		return jdbcTemplate.query(
				"""
				select p.id, p.title, p.text, p.image_path, p.likes_count 
				from posts p
				join tags t on p.id = t.post_id
				where t.text like ?
				""",
				(rs, rowNum) -> new Post(
					rs.getLong("id"),
					rs.getString("title"),
					rs.getString("text"),
					rs.getString("image_path"),
					rs.getInt("likes_count")
				),
				"%" + tag + "%"
			);
	}
	
	@Override
	public Post findById(Long id) {
	    return jdbcTemplate.queryForObject("select id, title, text, image_path, likes_count from posts where id = ?",
	    (rs, rowNum) -> new Post(
	            rs.getLong("id"),
	            rs.getString("title"),
	            rs.getString("text"),
	            rs.getString("image_path"),
	            rs.getInt("likes_count")
	        ),
	        id 
	    );
	}

	@Override
	public Long save(Post post) {
		String sql ="insert into posts (title, text, image_path, likes_count) values (?, ?, ?, ?)";
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	    jdbcTemplate.update(connection -> {
	        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        ps.setString(1, post.getTitle());
	        ps.setString(2, post.getText());
	        ps.setString(3, post.getImagePath());
	        ps.setInt(4, post.getLikesCount());
	        return ps;
	    }, keyHolder);
		    Long newId = keyHolder.getKey().longValue();
		    post.setId(newId);
		    return newId;
	}

	@Override
	public void update(Post post) {
		String sql = "update posts set title = ?, text = ?, image_path = ?, likes_count = ? where id = ?";
	    jdbcTemplate.update(sql,
	        post.getTitle(),
	        post.getText(),
	        post.getImagePath(),
	        post.getLikesCount(),
	        post.getId());
	}

	@Override
	public void updateLikes(Long postId, int delta) {
		jdbcTemplate.update("update posts set likes_count = likes_count + ? where id = ?", delta, postId); 
	}

	@Override
	public void deleteById(Long id) {
		jdbcTemplate.update("delete from posts where id = ?", id);

	}
}



