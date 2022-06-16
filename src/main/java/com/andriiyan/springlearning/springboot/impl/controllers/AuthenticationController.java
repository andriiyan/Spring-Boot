package com.andriiyan.springlearning.springboot.impl.controllers;

import com.andriiyan.springlearning.springboot.impl.model.transport.TokenResponseDTO;
import com.andriiyan.springlearning.springboot.impl.model.transport.UserCredentialsDTO;
import com.andriiyan.springlearning.springboot.security.JwtUtil;
import com.andriiyan.springlearning.springboot.security.SecurityConfiguration;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/login", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TokenResponseDTO handleSignIn(@RequestBody UserCredentialsDTO userCredentialsDTO) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userCredentialsDTO.username(), userCredentialsDTO.password());
        authenticationManager.authenticate(authToken);
        String token = jwtUtil.generateToken(userCredentialsDTO.username());
        return new TokenResponseDTO(token);
    }

    @GetMapping(value = "/logged", produces = { MediaType.APPLICATION_JSON_VALUE })
    @SecurityRequirement(name = SecurityConfiguration.SCHEME)
    public TokenResponseDTO redirectOAuth(@NonNull @RequestParam("token") String token) {
        return new TokenResponseDTO(token);
    }

}
