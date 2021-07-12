package com.ttsr.springshop.configuration.security;

import com.ttsr.springshop.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomPrincipal implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorityList;

    public CustomPrincipal(User user, Collection<? extends GrantedAuthority>  authorityList) {
        this.user = user;
        this.authorityList = authorityList;
    }

    public User getUser(){
        return this.user;
    }

    public boolean hasAuthority(String authority){
        for (GrantedAuthority grantedAuthority : authorityList) {
            if(grantedAuthority.getAuthority().equals(authority))
                return true;
        }
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
