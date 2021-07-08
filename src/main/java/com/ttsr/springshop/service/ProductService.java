package com.ttsr.springshop.service;

import com.ttsr.springshop.model.Product;
import com.ttsr.springshop.model.repository.ProductRepository;
import com.ttsr.springshop.service.spec.ProductSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll(Map<String, String> params){
        final Specification<Product> specification = build(params);
       return productRepository.findAll(specification);
    }

    public Product findById(UUID id) {
        return productRepository
                .findById(id)
                .map(Product::new)
                .orElseThrow(()->new NoSuchElementException(String.format("Product with id = %s wasn't found",id)));
    }

    public Product save(Product product) {
        Product updated = new Product();
        UUID productId = product.getId();
        UUID updatedId;
        if(product.getId() == null)
            updatedId = UUID.randomUUID();
        else updatedId = productId;

        updated.setId(updatedId);
        updated.setName(product.getName());
        updated.setCount(product.getCount());
        updated.setPrice(product.getPrice());
        updated = productRepository.save(updated);
        return updated;
    }

    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }

    public static Specification<Product> build(Map<String, String> params){
        return params.entrySet().stream()
                .filter(it-> StringUtils.hasText(it.getValue()))
                .map(it->{
                    if("name".equals(it.getKey())){
                        return ProductSpec.nameLike(it.getValue());
                    }
                    if("greater".equals(it.getKey())){
                        return ProductSpec.priceGreaterThen(Integer.parseInt(it.getValue()));
                    }
                    if("less".equals(it.getKey())){
                        return ProductSpec.priceLessThen(Integer.parseInt(it.getValue()));
                    }
                    if("category".equals(it.getKey())){
                        return ProductSpec.categoryNameEq(it.getValue());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse(null);
    }
}
