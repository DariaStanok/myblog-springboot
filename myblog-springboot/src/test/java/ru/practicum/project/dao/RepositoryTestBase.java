package ru.practicum.project.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class RepositoryTestBase {

	@Autowired
    protected JdbcTemplate jdbcTemplate;

    protected void resetDatabase() {
        jdbcTemplate.execute("delete from comments");
        jdbcTemplate.execute("delete from tags");
        jdbcTemplate.execute("delete from posts");
        jdbcTemplate.execute("alter table comments alter column id restart with 2");
        jdbcTemplate.execute("alter table tags alter column id restart with 2");
        jdbcTemplate.execute("alter table posts alter column id restart with 2");
    }

    protected void insertPost1() {
        jdbcTemplate.execute("insert into posts (id, title, text, image_path, likes_count) " +
                             "values(1, 'Title1', 'text', 'img.jpg', 5)");
    }

    protected void insertTag1() {
        jdbcTemplate.execute("insert into tags (id, post_id, text) values (1, 1, 'tag1')");
    }

    protected void insertComment1() {
        jdbcTemplate.execute("insert into comments (id, post_id, text) values (1, 1, 'like!')");
    }
}

