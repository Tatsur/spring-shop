package com.ttsr.springshop;

import com.ttsr.springshop.model.User;
import com.ttsr.springshop.model.repository.UserRepository;
import com.ttsr.springshop.service.UserService;
import com.vaadin.flow.server.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void loadUserByName() {
        UserDetails user = userService.loadUserByUsername("user1");
        Assertions.assertNotNull(user);
    }

    @Test
    public void saveUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setLogin("user1");
        Assertions.assertThrows(ServiceException.class, ()-> userService.store(user));
        user.setId(UUID.randomUUID());
        user.setLogin("user_66");
        Assertions.assertDoesNotThrow(()->userService.store(user));
    }

    @Test
    public void checkThrow() {
        var uuid = UUID.randomUUID();

        User userFromDb = new User();
        userFromDb.setId(uuid);
        userFromDb.setPhone("89154124124");
        userFromDb.setLogin("123");

        Mockito.doReturn(Optional.of(userFromDb))
                .when(userRepository)
                .findById(uuid);

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.findById(uuid));
    }
}
