package com.PetCare.Repository;

import com.PetCare.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PetRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PetRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    private final RowMapper<Pet> rowMapper = (rs, rowNum) -> new Pet(
            rs.getInt("pet_id"),
            rs.getInt("customer_id"),
            rs.getString("name"),
            rs.getString("species"),
            rs.getString("breed"),
            rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null,
            rs.getString("medical_notes")
    );

    public List<Pet> findAllByCustomerId(int customerId) {
        String sql = "SELECT * FROM pets WHERE customer_id = ?";
        return jdbcTemplate.query(sql, new Object[]{customerId}, rowMapper);
    }

    public Optional<Pet> findById(int id) {
        String sql = "SELECT * FROM pets WHERE pet_id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, rowMapper).stream().findFirst();
    }

    public int save(Pet pet) {
        String sql = "INSERT INTO pets (customer_id, name, species, breed, date_of_birth, medical_notes) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, pet.getCustomerId(), pet.getName(), pet.getSpecies(), pet.getBreed(), pet.getDateOfBirth(), pet.getMedicalNotes());
    }

    public int update(Pet pet) {
        String sql = "UPDATE pets SET customer_id = ?, name = ?, species = ?, breed = ?, date_of_birth = ?, medical_notes = ? WHERE pet_id = ?";
        return jdbcTemplate.update(sql, pet.getCustomerId(), pet.getName(), pet.getSpecies(), pet.getBreed(), pet.getDateOfBirth(), pet.getMedicalNotes(), pet.getPetId());
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM pets WHERE pet_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
