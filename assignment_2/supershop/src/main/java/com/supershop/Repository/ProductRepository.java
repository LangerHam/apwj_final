package com.supershop.Repository;

import com.supershop.Entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String find_by_id = "SELECT * FROM products WHERE id = ?";
    private static final String find_all = "SELECT * FROM products";
    private static final String save = "INSERT INTO products (name, category_id, price, quantity, expiry_date, discount, available) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String update = "UPDATE products SET name = ?, category_id = ?, price = ?, quantity = ?, expiry_date = ?, discount = ?, available = ? WHERE id = ?";
    private static final String delete_by_id = "DELETE FROM products WHERE id = ?";
    private static final String find_expiring = "SELECT * FROM products WHERE expiry_date <= ? AND available = TRUE";
    private static final String find_by_category = "SELECT * FROM products WHERE category_id = ? AND available = TRUE";

    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> Product.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .categoryId(rs.getLong("category_id"))
            .price(rs.getBigDecimal("price"))
            .quantity(rs.getInt("quantity"))
            .expiryDate(rs.getObject("expiry_date", LocalDate.class))
            .discount(rs.getBigDecimal("discount"))
            .available(rs.getBoolean("available"))
            .build();

    public Optional<Product> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(find_by_id, new Object[]{id}, productRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    public List<Product> findAll() {
        return jdbcTemplate.query(find_all, productRowMapper);
    }
    public Long save(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(save, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            ps.setLong(2, product.getCategoryId());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getQuantity());
            ps.setObject(5, product.getExpiryDate());
            ps.setBigDecimal(6, product.getDiscount());
            ps.setBoolean(7, product.isAvailable());
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
    }
    public int saveAll(List<Product> products) {
        int[][] result = jdbcTemplate.batchUpdate(save, products, 100, (ps, product) -> {
            ps.setString(1, product.getName());
            ps.setLong(2, product.getCategoryId());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getQuantity());
            ps.setObject(5, product.getExpiryDate());
            ps.setBigDecimal(6, product.getDiscount());
            ps.setBoolean(7, product.isAvailable());
        });
        return result.length;
    }
    public int update(Product product) {
        return jdbcTemplate.update(update,
                product.getName(),
                product.getCategoryId(),
                product.getPrice(),
                product.getQuantity(),
                product.getExpiryDate(),
                product.getDiscount(),
                product.isAvailable(),
                product.getId());
    }
    public int deleteById(Long id) {
        return jdbcTemplate.update(delete_by_id, id);
    }
    public List<Product> findProductsExpiringBy(LocalDate expiryDate) {
        return jdbcTemplate.query(find_expiring, new Object[]{expiryDate}, productRowMapper);
    }
    public List<Product> findByCategoryId(Long categoryId) {
        return jdbcTemplate.query(find_by_category, new Object[]{categoryId}, productRowMapper);
    }
}
