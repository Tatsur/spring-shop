package com.ttsr.springshop.dto;

import com.ttsr.springshop.model.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Component
@NoArgsConstructor
public class ProductDto {

    private UUID id;

    private String name;

    private int count;

    private BigDecimal price;

    public ProductDto(Product product) {
        setId(product.getId());
        setName(product.getName());
        setCount(product.getCount());
        setPrice(product.getPrice());
    }

    public ProductDto(ProductDto productDto) {
        setId(productDto.getId());
        setName(productDto.getName());
        setCount(productDto.getCount());
        setPrice(productDto.getPrice());
    }

    public void incrementCount() {
        this.count++;
    }

    public void decreaseCount() {
        this.count--;
    }
}
