package com.andriiyan.springlearning.springboot.impl.model;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public record UserDetailsImpl(@NonNull UserEntity userEntity) implements UserDetails, OAuth2User, Principal {

    public UserDetailsImpl(@NonNull UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("name", userEntity.getName());
        attributes.put("email", userEntity.getEmail());
        attributes.put("id", userEntity.getId());
        attributes.put("password", userEntity.getPassword());
        attributes.put("scope", userEntity.getScope());
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userEntity.grantedAuthority();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getName();
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

    @Override
    public String getName() {
        return userEntity.getName();
    }

    @Override
    public UserEntity userEntity() {
        return userEntity;
    }
}
