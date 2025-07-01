package com.PetCare.Repository;

import com.PetCare.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Employee> rowMapper = (rs, rowNum) -> new Employee(
            rs.getInt("employee_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            EmployeeRole.valueOf(rs.getString("role")),
            rs.getString("phone"),
            rs.getString("email"),
            rs.getDate("hire_date").toLocalDate(),
            rs.getString("password")
    );

    public List<Employee> findAll() {
        String sql = "SELECT employee_id, first_name, last_name, role, phone, email, hire_date, '' as password FROM employees";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Employee> findByEmail(String email) {
        String sql = "SELECT * FROM employees WHERE email = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, rowMapper).stream().findFirst();
    }

    public Optional<Employee> findById(int id) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, rowMapper).stream().findFirst();
    }

    public List<Employee> findByRole(EmployeeRole role) {
        String sql = "SELECT employee_id, first_name, last_name, role, phone, email, hire_date, '' as password FROM employees WHERE role = ?";
        return jdbcTemplate.query(sql, new Object[]{role.name()}, rowMapper);
    }

    public int save(Employee employee) {
        String sql = "INSERT INTO employees (first_name, last_name, role, phone, email, hire_date, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, employee.getFirstName(), employee.getLastName(), employee.getRole().name(), employee.getPhone(), employee.getEmail(), employee.getHireDate(), employee.getPassword());
    }

    public int update(Employee employee) {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, role = ?, phone = ?, email = ?, hire_date = ? WHERE employee_id = ?";
        return jdbcTemplate.update(sql, employee.getFirstName(), employee.getLastName(), employee.getRole().name(), employee.getPhone(), employee.getEmail(), employee.getHireDate(), employee.getEmployeeId());
    }

    public int updatePassword(String email, String newPassword) {
        String sql = "UPDATE employees SET password = ? WHERE email = ?";
        return jdbcTemplate.update(sql, newPassword, email);
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
