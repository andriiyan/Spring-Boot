package com.andriiyan.springlearning.springboot.impl.model.transport;

public class TokenResponseDTO {
    private String token;

    public TokenResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
