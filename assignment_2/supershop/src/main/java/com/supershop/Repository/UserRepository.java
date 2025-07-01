package com.supershop.Repository;

import com.supershop.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String find_by_username = "SELECT * FROM users WHERE username = ?";
    private static final String save_user = "INSERT INTO users (username, password, email, enabled) VALUES (?, ?, ?, ?)";
    private static final String save_user_role = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> User.builder()
            .id(rs.getLong("id"))
            .username(rs.getString("username"))
            .password(rs.getString("password"))
            .email(rs.getString("email"))
            .enabled(rs.getBoolean("enabled"))
            .build();

    public Optional<User> findByUsername(String username) {
        try {
            // Note: This only fetches the user. Roles must be fetched separately.
            return Optional.ofNullable(jdbcTemplate.queryForObject(find_by_username, new Object[]{username}, userRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    public Long save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(save_user, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setBoolean(4, user.isEnabled());
            return ps;
        }, keyHolder);
        Long userId = keyHolder.getKey().longValue();
        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> {
                jdbcTemplate.update(save_user_role, userId, role.getId());
            });
        }
        return userId;
    }
}
