package com.PetCare.Repository;

import com.PetCare.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SaleRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SaleRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    private final RowMapper<SaleItem> saleItemRowMapper = (rs, rowNum) -> new SaleItem(
            rs.getInt("sale_id"),
            rs.getInt("product_id"),
            rs.getInt("quantity"),
            rs.getBigDecimal("unit_price")
    );

    public int saveSale(Sale sale) {
        String sql = "INSERT INTO sales (customer_id, sale_date, total_amount) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, sale.getCustomerId(), sale.getSaleDate(), sale.getTotalAmount());
    }

    public int saveSaleItem(SaleItem item) {
        String sql = "INSERT INTO sale_items (sale_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, item.getSaleId(), item.getProductId(), item.getQuantity(), item.getUnitPrice());
    }

    public List<SaleItem> findSaleItemsBySaleId(int saleId) {
        return jdbcTemplate.query("SELECT * FROM sale_items WHERE sale_id = ?", new Object[]{saleId}, saleItemRowMapper);
    }
}
