package com.supershop.shop.Repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.supershop.shop.Entity.category;
import com.supershop.shop.Entity.Products;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class ProductRepository {
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String insert = "INSERT INTO products (Name, Category, Price, expiry_date) VALUES (?, ?, ?, ?)";
    private static final String update = "UPDATE products SET Name = ?, expiry_date = ? WHERE Id = ?";
    private static final String findexp = "SELECT * FROM products WHERE expiry_date <= ?";
    private static final String totalprice = "SELECT Category, SUM(Price) as total_price FROM products GROUP BY Category";

    private static final class productRowMapper implements RowMapper<Products> {
        @Override
        public Products mapRow(ResultSet rs, int rowNum) throws SQLException {
            Products products = new Products();
            products.setId(rs.getInt("Id"));
            products.setName(rs.getString("Name"));
            products.setCategory(category.valueOf(rs.getString("Category")));
            products.setPrice(rs.getBigDecimal("Price"));
            products.setExpiration_date(rs.getDate("Expiration_Date").toLocalDate());
            return products;
        }
    }

    public int save(Products products) {
        return jdbcTemplate.update(insert, products.getName(), products.getCategory().name(),
                products.getPrice(), products.getExpiration_date());
    }

    public int[][] saveAll(List<Products> products) {
        return jdbcTemplate.batchUpdate(insert, products, products.size(), (ps, product) -> {
            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory().name());
            ps.setBigDecimal(3, product.getPrice());
            ps.setDate(4, Date.valueOf(product.getExpiration_date()));
        });
    }

    public int update(Products products) {
        return jdbcTemplate.update(update, products.getName(), products.getCategory().name(),
                products.getPrice(), products.getExpiration_date(), products.getId());
    }

    public List<Products> findExpire(LocalDate date) {
        return jdbcTemplate.query(findexp, new productRowMapper(), date);
    }

    public List<Map<String, Object>> totalPriceByCategory() {
        return jdbcTemplate.queryForList(totalprice);
    }
}
