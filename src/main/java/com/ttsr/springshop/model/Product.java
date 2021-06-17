package com.ttsr.springshop.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    public Product(Product product) {
        setId(product.getId());
        setName(product.getName());
        setCount(product.getCount());
    }

    public Product(Cart cart) {
        setId(cart.getProductId());
        setName(cart.getProductName());
        setCount(cart.getCount());
    }

    public void incrementCount() {
        this.count++;
    }

    public void decreaseCount() {
        this.count--;
    }
}
