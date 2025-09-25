package com.gary.mcp.shopping_cart;

import com.gary.mcp.shopping_cart.tools.ShoppingCartMcpService;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ShoppingCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartApplication.class, args);
	}

    @Bean
    public List<ToolCallback> shoppingCartToolCallback(ShoppingCartMcpService shoppingCartMcpService) {
        return List.of(ToolCallbacks.from(shoppingCartMcpService));
    }

}
