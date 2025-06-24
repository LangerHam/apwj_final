package com.supershop.Repository;

import com.supershop.Entity.Product;
import com.supershop.Entity.WishlistItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class WishlistRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WishlistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String add_item = "INSERT INTO wishlist_items (user_id, product_id) VALUES (?, ?)";
    private static final String remove_item = "DELETE FROM wishlist_items WHERE user_id = ? AND product_id = ?";
    private static final String find_products_by_user_id = "SELECT p.* FROM products p JOIN wishlist_items w ON p.id = w.product_id WHERE w.user_id = ?";

    public void addItem(WishlistItem item) {
        jdbcTemplate.update(add_item, item.getUserId(), item.getProductId());
    }
    public int removeItem(Long userId, Long productId) {
        return jdbcTemplate.update(remove_item, userId, productId);
    }
    public List<Product> findProductsByUserId(Long userId) {
        return jdbcTemplate.query(find_products_by_user_id, (rs, rowNum) -> Product.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .categoryId(rs.getLong("category_id"))
                .price(rs.getBigDecimal("price"))
                .quantity(rs.getInt("quantity"))
                .expiryDate(rs.getObject("expiry_date", LocalDate.class))
                .discount(rs.getBigDecimal("discount"))
                .available(rs.getBoolean("available"))
                .build(), userId);
    }
}
