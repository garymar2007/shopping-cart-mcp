package com.gary.mcp.shopping_cart.repository;

import com.gary.mcp.shopping_cart.entity.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartItemRepository extends MongoRepository<CartItem, String> {
    CartItem findByProductId(String productId);
    void deleteByProductId(String productId);
}
