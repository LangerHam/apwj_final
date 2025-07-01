package com.PetCare.Repository;

import com.PetCare.Entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Customer> rowMapper = (rs, rowNum) -> new Customer(
            rs.getInt("customer_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("phone"),
            rs.getString("email"),
            rs.getString("address"),
            rs.getString("password")
    );

    public Optional<Customer> findByEmail(String email) {
        String sql = "SELECT * FROM customers WHERE email = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, rowMapper).stream().findFirst();
    }

    public Optional<Customer> findById(int id) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, rowMapper).stream().findFirst();
    }

    public List<Customer> findAll() {
        String sql = "SELECT customer_id, first_name, last_name, phone, email, address, '' as password FROM customers";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int save(Customer customer) {
        String sql = "INSERT INTO customers (first_name, last_name, phone, email, address, password) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, customer.getFirstName(), customer.getLastName(), customer.getPhone(), customer.getEmail(), customer.getAddress(), customer.getPassword());
    }

    public int update(Customer customer) {
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, phone = ?, email = ?, address = ? WHERE customer_id = ?";
        return jdbcTemplate.update(sql, customer.getFirstName(), customer.getLastName(), customer.getPhone(), customer.getEmail(), customer.getAddress(), customer.getCustomerId());
    }

    public int updatePassword(String email, String newPassword) {
        String sql = "UPDATE customers SET password = ? WHERE email = ?";
        return jdbcTemplate.update(sql, newPassword, email);
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
