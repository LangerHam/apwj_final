package com.supershop.Repository;

import com.supershop.Entity.Order;
import com.supershop.Entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String save_order = "INSERT INTO orders (user_id, total_amount, status, order_date) VALUES (?, ?, ?, ?)";
    private static final String save_order_item = "INSERT INTO order_items (order_id, product_id, quantity, price_per_unit, discount_applied) VALUES (?, ?, ?, ?, ?)";
    private static final String find_orders_by_user_id = "SELECT * FROM orders WHERE user_id = ?";
    private static final String find_all = "SELECT * FROM orders";
    private static final String find_items_by_id = "SELECT * FROM order_items WHERE order_id = ?";

    private List<OrderItem> findOrderItemsByOrderId(Long orderId) {
        return jdbcTemplate.query(find_items_by_id, (rs, rowNum) -> OrderItem.builder()
                .id(rs.getLong("id"))
                .orderId(rs.getLong("order_id"))
                .productId(rs.getLong("product_id"))
                .quantity(rs.getInt("quantity"))
                .pricePerUnit(rs.getBigDecimal("price_per_unit"))
                .discountApplied(rs.getBigDecimal("discount_applied"))
                .build(), orderId);
    }
    public Long saveOrder(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(save_order, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotalAmount());
            ps.setString(3, order.getStatus());
            ps.setTimestamp(4, Timestamp.valueOf(order.getOrderDate()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
    public void saveOrderItems(List<OrderItem> items) {
        jdbcTemplate.batchUpdate(save_order_item, items, 100, (ps, item) -> {
            ps.setLong(1, item.getOrderId());
            ps.setLong(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getPricePerUnit());
            ps.setBigDecimal(5, item.getDiscountApplied());
        });
    }
    public List<Order> findByUserId(Long userId) {
        List<Order> orders = jdbcTemplate.query(find_orders_by_user_id, (rs, rowNum) -> Order.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .orderDate(rs.getTimestamp("order_date").toLocalDateTime())
                .totalAmount(rs.getBigDecimal("total_amount"))
                .status(rs.getString("status"))
                .build(), userId);
        orders.forEach(order -> {
            List<OrderItem> items = findOrderItemsByOrderId(order.getId());
            order.setItems(items);
        });
        return orders;
    }
    public List<Order> findAll() {
        List<Order> orders = jdbcTemplate.query(find_all, (rs, rowNum) -> Order.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .orderDate(rs.getTimestamp("order_date").toLocalDateTime())
                .totalAmount(rs.getBigDecimal("total_amount"))
                .status(rs.getString("status"))
                .build());
        orders.forEach(order -> {
            List<OrderItem> items = findOrderItemsByOrderId(order.getId());
            order.setItems(items);
        });
        return orders;
    }
}
