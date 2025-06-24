package com.supershop.Repository;

import com.supershop.Entity.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CartRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CartRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String find_by_user_and_product = "SELECT * FROM cart_items WHERE user_id = ? AND product_id = ?";
    private static final String save = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
    private static final String update = "UPDATE cart_items SET quantity = ? WHERE id = ?";
    private static final String remove_item = "DELETE FROM cart_items WHERE user_id = ? AND product_id = ?";
    private static final String find_by_user_id = "SELECT * FROM cart_items WHERE user_id = ?";
    private static final String clear_cart = "DELETE FROM cart_items WHERE user_id = ?";

    private final RowMapper<CartItem> cartItemRowMapper = (rs, rowNum) -> CartItem.builder()
            .id(rs.getLong("id"))
            .userId(rs.getLong("user_id"))
            .productId(rs.getLong("product_id"))
            .quantity(rs.getInt("quantity"))
            .build();

    public Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(find_by_user_and_product, new Object[]{userId, productId}, cartItemRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    public void save(CartItem item) {
        jdbcTemplate.update(save, item.getUserId(), item.getProductId(), item.getQuantity());
    }
    public int update(CartItem item) {
        return jdbcTemplate.update(update, item.getQuantity(), item.getId());
    }
    public int removeItem(Long userId, Long productId) {
        return jdbcTemplate.update(remove_item, userId, productId);
    }
    public List<CartItem> findByUserId(Long userId) {
        return jdbcTemplate.query(find_by_user_id, new Object[]{userId}, cartItemRowMapper);
    }
    public void clearCart(Long userId) {
        jdbcTemplate.update(clear_cart, userId);
    }
}
