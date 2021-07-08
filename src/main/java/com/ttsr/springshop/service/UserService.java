package com.ttsr.springshop.service;

import com.ttsr.springshop.model.Role;
import com.ttsr.springshop.model.User;
import com.ttsr.springshop.model.repository.UserRepository;
import com.vaadin.flow.server.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(()->
                        new UsernameNotFoundException(String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRole())
        );
    }

    public User findById(UUID id){
        return userRepository.findById(id).orElseThrow(()-> new NoSuchElementException(String.format("User with id '%s' not found",id)));
    }

    public void store(User user) throws ServiceException{
        if (findByUsername(user.getLogin()).isEmpty()) {
            userRepository.save(user);
        }
        throw new ServiceException("user already exists");
    }

    public String isUsernameValid(String username){
        if(findByUsername(username).isEmpty()) return null;
        return "username already exists";
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByLogin(username);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role ->
                new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
