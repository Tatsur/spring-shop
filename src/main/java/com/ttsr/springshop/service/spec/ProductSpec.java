package com.ttsr.springshop.service.spec;

import com.ttsr.springshop.model.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductSpec{
    public static Specification<Product> nameEq(String name){
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"),name);
    }
    public static Specification<Product> nameLike(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),name.toUpperCase() + "%");
    }
    public static Specification<Product> priceGreaterThen(Integer price) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("price"), price);
    }
    public static Specification<Product> priceLessThen(Integer price) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("price"), price);
    }
    public static Specification<Product> categoryNameEq(String category) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("name"), category);
    }
}
