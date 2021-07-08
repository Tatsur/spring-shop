package com.ttsr.springshop;

import com.ttsr.springshop.model.Cart;
import com.ttsr.springshop.service.CartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest
public class CartServiceTest {
    @Autowired
    private CartService cartService;

    @BeforeEach
    public void init() {
        cartService.clear();
    }

    @Test
    public void cartFillingTest() {
        for(int i = 0; i < 10; i++) {
            var product = new Cart();
            product.setProductId(UUID.randomUUID());
            product.setPrice(new BigDecimal(100 + (long) i * 10));
            product.setProductName("Product #" + i);
            cartService.getProducts().add(product);
        }

        Assertions.assertEquals(10, cartService.getProducts().size());
    }

    @Test
    public void clearCart() {
        for(int i = 0; i < 5; i++) {
            var product = new Cart();
            product.setProductId(UUID.randomUUID());
            product.setPrice(new BigDecimal(100 + (long) i * 10));
            product.setProductName("Product #" + i);
            cartService.getProducts().add(product);
        }

        Assertions.assertEquals(5, cartService.getProducts().size());
        cartService.clear();
        Assertions.assertEquals(0, cartService.getProducts().size());
    }
}
