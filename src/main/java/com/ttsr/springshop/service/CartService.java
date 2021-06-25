package com.ttsr.springshop.service;

import com.ttsr.springshop.model.Cart;
import com.ttsr.springshop.model.Product;
import com.ttsr.springshop.model.repository.CartRepository;
import com.ttsr.springshop.model.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final List<Cart> products;

    private final CartRepository cartRepository;

    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        products = cartRepository.findAll().stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void addProduct(Product product) {
        Product p = productService.findById(product.getId());
        products.add(new Cart(p));
    }

    public void addProduct(Set<Product> products) {
        Set<Product> dbProducts = products.stream()
                .filter(it -> productService.findById(it.getId()) != null)
                .collect(Collectors.toSet());
        this.products.addAll(dbProducts.stream().map(
                it -> {
                    var newProduct = new Cart();
                    newProduct.setCount(1);
                    newProduct.setProductName(it.getName());
                    newProduct.setId(it.getId());
                    return newProduct;
                }
        ).collect(Collectors.toList()));
    }

    public void deleteProduct(Product product) {
        products.removeIf(p -> p.getId().equals(product.getId()));
    }

    public void increaseProductCount(Cart product) {
        for(Cart innerProduct: products) {
            if(product.getId().equals(innerProduct.getId())) {
                innerProduct.incrementCount();
                return;
            }
        }
    }

    public List<Cart> save(List<Product> products) {
        List<Cart> cartList = products.stream().map(Cart::new).collect(Collectors.toList());
        cartRepository.saveAll(cartList);
        return cartList;
    }
    public List<Cart> saveCart(List<Cart> products) {
        cartRepository.saveAll(products);
        return products;
    }

    public void decreaseProductCount(Cart product) {
        for(Cart innerProduct: products) {
            if(product.getId().equals(innerProduct.getId())) {
                innerProduct.decreaseCount();
                return;
            }
        }
    }

    public List<Cart> getProducts() {
        return products;
    }
}
