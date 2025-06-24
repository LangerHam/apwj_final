package com.supershop.Api;

import com.supershop.Entity.CartItem;
import com.supershop.Entity.User;
import com.supershop.Repository.UserRepository;
import com.supershop.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/cart")
@PreAuthorize("hasRole('USER')")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserRepository userRepository;

    private Long getAuthenticatedUserId(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + userDetails.getUsername()));
        return user.getId();
    }
    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getAuthenticatedUserId(userDetails);
        return ResponseEntity.ok(cartService.getCartItems(userId));
    }
    @PostMapping
    public ResponseEntity<Void> addToCart(@RequestParam Long productId, @RequestParam int quantity, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getAuthenticatedUserId(userDetails);
        cartService.addOrUpdateItem(userId, productId, quantity);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping
    public ResponseEntity<Void> removeFromCart(@RequestParam Long productId, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getAuthenticatedUserId(userDetails);
        cartService.removeItem(userId, productId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getAuthenticatedUserId(userDetails);
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
