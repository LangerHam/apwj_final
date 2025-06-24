package com.supershop.Service;

import com.supershop.Entity.Product;
import com.supershop.Entity.WishlistItem;
import com.supershop.Repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    @Autowired
    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public void addToWishlist(Long userId, Long productId) {
        WishlistItem newItem = WishlistItem.builder()
                .userId(userId)
                .productId(productId)
                .build();
        wishlistRepository.addItem(newItem);
    }
    public void removeFromWishlist(Long userId, Long productId) {
        wishlistRepository.removeItem(userId, productId);
    }
    public List<Product> getWishlist(Long userId) {
        return wishlistRepository.findProductsByUserId(userId);
    }
}
