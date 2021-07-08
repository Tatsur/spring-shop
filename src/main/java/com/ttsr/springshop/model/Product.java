package com.ttsr.springshop.model;

import com.ttsr.springshop.dto.ProductDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String name;

    private int count;

    private BigDecimal price;

    public Product(Product product) {
        setId(product.getId());
        setName(product.getName());
        setCount(product.getCount());
        setPrice(product.getPrice());
    }

    public Product(ProductDto product) {
        setId(product.getId());
        setName(product.getName());
        setCount(product.getCount());
        setPrice(product.getPrice());
    }

    public Product(Cart cart) {
        setId(cart.getProductId());
        setName(cart.getProductName());
        setCount(cart.getCount());
        setPrice(cart.getPrice());
    }

    public void incrementCount() {
        this.count++;
    }

    public void decreaseCount() {
        this.count--;
    }
}
