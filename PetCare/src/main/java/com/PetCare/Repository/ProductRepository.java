package com.PetCare.Repository;

import com.PetCare.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    private final RowMapper<Product> rowMapper = (rs, rowNum) -> new Product(
            rs.getInt("product_id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getBigDecimal("price"),
            rs.getInt("stock_quantity")
    );

    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM products", rowMapper);
    }

    public Optional<Product> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM products WHERE product_id = ?", new Object[]{id}, rowMapper).stream().findFirst();
    }

    public int updateStock(int productId, int newQuantity) {
        return jdbcTemplate.update("UPDATE products SET stock_quantity = ? WHERE product_id = ?", newQuantity, productId);
    }
}
