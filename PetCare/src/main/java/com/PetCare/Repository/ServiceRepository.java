package com.PetCare.Repository;

import com.PetCare.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ServiceRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ServiceRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    private final RowMapper<Service> rowMapper = (rs, rowNum) -> new Service(
            rs.getInt("service_id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getBigDecimal("price")
    );

    public List<Service> findAll() {
        return jdbcTemplate.query("SELECT * FROM services", rowMapper);
    }

    public Optional<Service> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM services WHERE service_id = ?", new Object[]{id}, rowMapper).stream().findFirst();
    }
}
