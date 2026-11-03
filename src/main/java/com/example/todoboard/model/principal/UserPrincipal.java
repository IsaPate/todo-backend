package com.example.todoboard.model.principal;

import java.util.Collection;
import java.util.Collections;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.todoboard.model.User;

public class UserPrincipal implements UserDetails {

    private User user;

    public UserPrincipal(User user1) {
        this.user = user1;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String userRoleStr = user.getUserRole().toString();
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + userRoleStr));
    }

    @Override
    public @Nullable String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    public Integer getUserId() {
        return this.user.getId();
    }
}
