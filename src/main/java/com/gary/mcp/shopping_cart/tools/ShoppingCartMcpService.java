package com.gary.mcp.shopping_cart.tools;

import com.gary.mcp.shopping_cart.entity.CartItem;
import com.gary.mcp.shopping_cart.repository.CartItemRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartMcpService {
    private CartItemRepository cartItemRepository;

    public ShoppingCartMcpService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    // tools
    // catalog service
    private static final Map<String, Double> PRODUCTS = Map.of(
            "iPhone", 79999.0,
            "Samsung Galaxy", 69999.0,
            "Google Pixel", 59999.0,
            "OnePlus", 49999.0,
            "Sony Xperia", 89999.0,
            "MacBook Pro", 129999.0,
            "Dell XPS", 119999.0,
            "HP Spectre", 109999.0,
            "Lenovo ThinkPad", 99999.0,
            "Asus ZenBook", 89999.0
    );

    @Tool(name = "addToCart",
            description = "Add a product to the shopping cart.  If product exists, update the quantity.")
    public String addToCart(@ToolParam String productName, @ToolParam int quantity) {
        if (!PRODUCTS.containsKey(productName)) {
            return "Product not found";
        }
        if (quantity <= 0) {
            return "Quantity must be greater than zero";
        }
        var price = PRODUCTS.get(productName);
        var cartItem = cartItemRepository.findByProductId(productName);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setProductId(productName);
            cartItem.setProductName(productName);
            cartItem.setQuantity(quantity);
        }
        cartItem.setPrice(cartItem.getQuantity() * price);
        cartItemRepository.save(cartItem);
        return quantity + " " + productName + " added to cart. Total price: $" + (PRODUCTS.get(productName) * quantity);
    }

    @Tool(name = "removeFromCart", description = "Remove a product from the shopping cart.")
    public String removeFromCart(@ToolParam String productName) {
        var existingItem = cartItemRepository.findByProductId(productName);
        if (existingItem == null) {
            return "Product not found in cart.";
        }
        cartItemRepository.deleteByProductId(productName);
        return productName + " removed from cart.";
    }

    @Tool(name = "getCarts", description = "Get the current items in the shopping cart.")
    public List<CartItem> getCart() {
        return cartItemRepository.findAll();
    }

    @Tool(name = "clearCart", description = "Clear all items from the shopping cart.")
    public void clearCart() {
        cartItemRepository.deleteAll();
    }

    @Tool(name = "getCartTotal", description = "Get the total price of items in the shopping cart.")
    public double getCartTotal() {
        return cartItemRepository.findAll().stream()
                .mapToDouble(item -> PRODUCTS.get(item.getProductId()) * item.getQuantity())
                .sum();
    }
}
