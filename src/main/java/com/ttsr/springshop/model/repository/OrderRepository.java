package com.ttsr.springshop.model.repository;

import com.ttsr.springshop.model.Order;
import com.ttsr.springshop.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID> {
    List<Order> findAll();
}
