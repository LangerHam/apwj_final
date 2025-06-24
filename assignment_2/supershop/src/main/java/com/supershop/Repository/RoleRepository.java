package com.supershop.Repository;

import com.supershop.Entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class RoleRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String find_roles_by_user_id = "SELECT r.id, r.name FROM roles r JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?";

    private final RowMapper<Role> roleRowMapper = (rs, rowNum) -> Role.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .build();

    public Set<Role> findRolesByUserId(Long userId) {
        return new HashSet<>(jdbcTemplate.query(find_roles_by_user_id, new Object[]{userId}, roleRowMapper));
    }
}
