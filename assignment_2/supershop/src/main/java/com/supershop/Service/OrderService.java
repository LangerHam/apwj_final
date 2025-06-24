package com.supershop.Service;

import com.supershop.Entity.CartItem;
import com.supershop.Entity.Order;
import com.supershop.Entity.OrderItem;
import com.supershop.Entity.Product;
import com.supershop.Repository.CartRepository;
import com.supershop.Repository.OrderRepository;
import com.supershop.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final ProductService productService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CartRepository cartRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    @Transactional
    public Order checkout(Long userId) {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot checkout.");
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + cartItem.getProductId()));
            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }
            BigDecimal pricePerUnit = product.getPrice();
            BigDecimal discount = getApplicableDiscount(product);
            BigDecimal discountedPrice = pricePerUnit.subtract(pricePerUnit.multiply(discount));
            totalAmount = totalAmount.add(discountedPrice.multiply(new BigDecimal(cartItem.getQuantity())));

            OrderItem orderItem = OrderItem.builder()
                    .productId(product.getId())
                    .quantity(cartItem.getQuantity())
                    .pricePerUnit(pricePerUnit)
                    .discountApplied(discount)
                    .build();
            orderItems.add(orderItem);
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.update(product);
        }
        Order order = Order.builder()
                .userId(userId)
                .orderDate(LocalDateTime.now())
                .totalAmount(totalAmount)
                .status("COMPLETED")
                .build();
        Long orderId = orderRepository.saveOrder(order);
        order.setId(orderId);
        for (OrderItem item : orderItems) {
            item.setOrderId(orderId);
        }
        orderRepository.saveOrderItems(orderItems);
        order.setItems(orderItems);
        cartRepository.clearCart(userId);

        return order;
    }
    private BigDecimal getApplicableDiscount(Product product) {
        if (product.getExpiryDate() != null && product.getExpiryDate().isBefore(LocalDate.now().plusDays(8))) {
            return new BigDecimal("0.20"); // 20%
        }
        return product.getDiscount() != null ? product.getDiscount() : BigDecimal.ZERO;
    }
    public List<Order> getOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
