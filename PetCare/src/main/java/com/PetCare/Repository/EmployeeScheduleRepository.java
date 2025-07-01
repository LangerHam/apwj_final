package com.PetCare.Repository;

import com.PetCare.Entity.EmployeeSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeScheduleRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeScheduleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<EmployeeSchedule> rowMapper = (rs, rowNum) -> new EmployeeSchedule(
            rs.getInt("schedule_id"),
            rs.getInt("employee_id"),
            DayOfWeek.valueOf(rs.getString("day_of_week")),
            rs.getTime("start_time").toLocalTime(),
            rs.getTime("end_time").toLocalTime()
    );

    public int save(EmployeeSchedule schedule) {
        String sql = "INSERT INTO employee_schedules (employee_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, schedule.getEmployeeId(), schedule.getDayOfWeek().name(), schedule.getStartTime(), schedule.getEndTime());
    }

    public Optional<EmployeeSchedule> findByEmployeeIdAndDay(int employeeId, DayOfWeek dayOfWeek) {
        String sql = "SELECT * FROM employee_schedules WHERE employee_id = ? AND day_of_week = ?";
        return jdbcTemplate.query(sql, new Object[]{employeeId, dayOfWeek.name()}, rowMapper).stream().findFirst();
    }

    public List<EmployeeSchedule> findAllByEmployeeId(int employeeId) {
        String sql = "SELECT * FROM employee_schedules WHERE employee_id = ?";
        return jdbcTemplate.query(sql, new Object[]{employeeId}, rowMapper);
    }
}
