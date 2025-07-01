package com.PetCare.Repository;

import com.PetCare.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AppointmentRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AppointmentRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    private final RowMapper<Appointment> rowMapper = (rs, rowNum) -> new Appointment(
            rs.getInt("appointment_id"),
            rs.getInt("customer_id"),
            rs.getInt("pet_id"),
            rs.getInt("employee_id"),
            rs.getInt("service_id"),
            rs.getTimestamp("appointment_date").toLocalDateTime(),
            rs.getString("status"),
            rs.getString("notes")
    );

    public int save(Appointment appointment) {
        String sql = "INSERT INTO appointments (customer_id, pet_id, employee_id, service_id, appointment_date, status, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, appointment.getCustomerId(), appointment.getPetId(), appointment.getEmployeeId(), appointment.getServiceId(), appointment.getAppointmentDate(), appointment.getStatus(), appointment.getNotes());
    }

    public List<Appointment> findAllByCustomerId(int customerId) {
        return jdbcTemplate.query("SELECT * FROM appointments WHERE customer_id = ?", new Object[]{customerId}, rowMapper);
    }

    public List<Appointment> findAllByEmployeeId(int employeeId) {
        String sql = "SELECT * FROM appointments WHERE employee_id = ?";
        return jdbcTemplate.query(sql, new Object[]{employeeId}, rowMapper);
    }
}
