package com.ttsr.springshop.service;

import com.ttsr.springshop.model.Product;
import com.ttsr.springshop.model.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll(){
       return productRepository.findAll();
    }

    public Product findById(UUID id) {
        return productRepository
                .findById(id)
                .map(Product::new)
                .orElseThrow(()->new NoSuchElementException(String.format("Product with id = %s wasn't found",id)));
    }

    public Product save(Product product) {
        Product updated = new Product();
        updated.setName(product.getName());
        updated.setCount(product.getCount());
        updated = productRepository.save(updated);
        return updated;
    }

    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }
}
