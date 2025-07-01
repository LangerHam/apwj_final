package com.supershop.Service;

import com.supershop.Entity.CartItem;
import com.supershop.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void addOrUpdateItem(Long userId, Long productId, int quantity) {
        Optional<CartItem> existingItemOpt = cartRepository.findByUserIdAndProductId(userId, productId);
        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartRepository.update(existingItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .userId(userId)
                    .productId(productId)
                    .quantity(quantity)
                    .build();
            cartRepository.save(newItem);
        }
    }
    public void removeItem(Long userId, Long productId) {
        cartRepository.removeItem(userId, productId);
    }
    public List<CartItem> getCartItems(Long userId) {
        return cartRepository.findByUserId(userId);
    }
    public void clearCart(Long userId) {
        cartRepository.clearCart(userId);
    }
}
