package com.inventory.controllers;

import com.inventory.models.CartItem;
import com.inventory.models.User;
import com.inventory.payload.MessageResponse;
import com.inventory.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
    }

    // --- NEW "ADD TO CART" ENDPOINT ---
    @PostMapping("/cart")
    public ResponseEntity<?> addToCart(@RequestBody CartItem newCartItem) {
        User user = getCurrentUser();
        List<CartItem> cart = user.getCart();

        Optional<CartItem> existingItem = cart.stream()
                .filter(item -> item.getProductId().equals(newCartItem.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + newCartItem.getQuantity());
        } else {
            cart.add(newCartItem);
        }

        user.setCart(cart);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Item added to cart successfully!"));
    }

    @GetMapping("/cart")
    public ResponseEntity<List<CartItem>> getCart() {
        User user = getCurrentUser();
        return ResponseEntity.ok(user.getCart());
    }

    // --- NEW "REMOVE FROM CART" ENDPOINT ---
    @DeleteMapping("/cart/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable String productId) {
        User user = getCurrentUser();
        List<CartItem> cart = user.getCart();

        // Remove the item if it matches the productId
        cart.removeIf(item -> item.getProductId().equals(productId));

        user.setCart(cart);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Item removed from cart successfully!"));
    }
}