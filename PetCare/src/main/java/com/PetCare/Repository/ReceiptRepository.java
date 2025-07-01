package com.PetCare.Repository;

import com.PetCare.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReceiptRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReceiptRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public int save(Receipt receipt) {
        String sql = "INSERT INTO receipts (sale_id, appointment_id, receipt_date, file_path) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, receipt.getSaleId(), receipt.getAppointmentId(), receipt.getReceiptDate(), receipt.getFilePath());
    }
}