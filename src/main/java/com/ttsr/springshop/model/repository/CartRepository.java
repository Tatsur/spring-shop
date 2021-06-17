package com.ttsr.springshop.model.repository;

import com.ttsr.springshop.model.Cart;
import com.ttsr.springshop.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends CrudRepository<Cart, UUID> {
    List<Cart> findAll();
}
