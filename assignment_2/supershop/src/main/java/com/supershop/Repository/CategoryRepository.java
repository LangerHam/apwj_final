package com.supershop.Repository;

import com.supershop.Entity.Category;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String find_by_id = "SELECT * FROM categories WHERE id = ?";
    private static final String find_all = "SELECT * FROM categories";
    private static final String find_by_name = "SELECT * FROM categories WHERE name = ?";
    private static final String save = "INSERT INTO categories (name) VALUES (?)";

    private final RowMapper<Category> categoryRowMapper = (rs, rowNum) -> Category.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .build();

    public Optional<Category> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(find_by_id, new Object[]{id}, categoryRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    public List<Category> findAll() {
        return jdbcTemplate.query(find_all, categoryRowMapper);
    }
    public Optional<Category> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(find_by_name, new Object[]{name}, categoryRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    public Long save(Category category) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(save, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, category.getName());
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
    }
}
