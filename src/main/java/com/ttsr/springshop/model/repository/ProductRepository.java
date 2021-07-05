package com.ttsr.springshop.model.repository;

import com.ttsr.springshop.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByName(String name);

    List<Product> findAllByPriceGreaterThan(BigDecimal min);

    List<Product> findAllByPriceLessThan(BigDecimal max);

    List<Product> findAllByPriceBetween(BigDecimal min, BigDecimal max);
}
