package com.ttsr.springshop.dto;

import com.ttsr.springshop.model.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ProductDto {

    private UUID id;

    private String name;

    private int count;

    public ProductDto(Product product) {
        setId(product.getId());
        setName(product.getName());
        setCount(product.getCount());
    }

    public ProductDto(ProductDto productDto) {
        setId(productDto.getId());
        setName(productDto.getName());
        setCount(productDto.getCount());
    }

    public void incrementCount() {
        this.count++;
    }

    public void decreaseCount() {
        this.count--;
    }
}
