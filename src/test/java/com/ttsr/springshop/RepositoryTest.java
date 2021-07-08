package com.ttsr.springshop;

import com.ttsr.springshop.model.Product;
import com.ttsr.springshop.model.repository.ProductRepository;
import com.ttsr.springshop.model.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@DataJpaTest
@ActiveProfiles("test")
public class RepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void productRepositorySaveTest() {
        var product = new Product();
        product.setId(UUID.randomUUID());
        product.setPrice(BigDecimal.TEN);
        product.setName("prod1");

        var product2 = new Product();
        product2.setId(UUID.randomUUID());
        product2.setPrice(BigDecimal.TEN);
        product2.setName("prod2");
        entityManager.persist(product);
        entityManager.persist(product2);
        entityManager.flush();

        List<Product> productList = productRepository.findAll();

        Assertions.assertEquals(2, productList.size());
        Assertions.assertEquals("prod2", productList.get(productList.size() - 1).getName());
        Assertions.assertEquals("prod1", productList.get(0).getName());
    }

    @Test
    public void initDbTest() {
        var userList = userRepository.findAll();
        Assertions.assertEquals(2, userList.size());
    }
}
