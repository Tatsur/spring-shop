package com.ttsr.springshop;

import com.ttsr.springshop.model.Product;
import com.ttsr.springshop.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Test
    public void createProduct() {
            var product = new Product();
            product.setId(UUID.randomUUID());
            product.setPrice(new BigDecimal(150));
            product.setName("Test prod");
            productService.save(product);
        Assertions.assertNotNull(productService.findById(product.getId()));
    }

    @Test
    public void createProducts() {
        for(int i = 0; i < 5; i++) {
            var product = new Product();
            product.setId(UUID.randomUUID());
            product.setPrice(new BigDecimal(100 + (long) i * 10));
            product.setName("Product #" + i);
            productService.save(product);
        }
        Assertions.assertEquals(10, productService.findAll(new HashMap<>()).size());
    }

    @Test
    public void deleteById(){
        var product = new Product();
        UUID uuid = UUID.randomUUID();
        product.setId(uuid);
        product.setPrice(new BigDecimal(100));
        product.setName("Product for del");
        productService.save(product);
        productService.deleteById(uuid);
        Assertions.assertNull(productService.findById(uuid));
    }
}
