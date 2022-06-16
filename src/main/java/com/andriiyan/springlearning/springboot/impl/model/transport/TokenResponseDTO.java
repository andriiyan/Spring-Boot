package com.andriiyan.springlearning.springboot.impl.model.transport;

import org.springframework.lang.NonNull;

public record TokenResponseDTO(@NonNull String token) {
    public TokenResponseDTO(@NonNull String token) {
        this.token = token;
    }

    @Override
    public String token() {
        return token;
    }
}
