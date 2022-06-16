package com.andriiyan.springlearning.springboot.impl.model.transport;

import org.springframework.lang.NonNull;

public record UserCredentialsDTO(@NonNull String username, @NonNull String password) {
    public UserCredentialsDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }
}
