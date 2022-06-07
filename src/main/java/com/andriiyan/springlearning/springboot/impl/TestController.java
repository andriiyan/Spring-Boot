package com.andriiyan.springlearning.springboot.impl;

import com.andriiyan.springlearning.springboot.api.model.User;
import com.andriiyan.springlearning.springboot.security.JwtUtil;
import com.andriiyan.springlearning.springboot.impl.model.transport.TokenResponseDTO;
import com.andriiyan.springlearning.springboot.impl.model.transport.UserCredentialsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class TestController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    private static UserDetails getUserDetailsFromAuthentication() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping("/login")
    public TokenResponseDTO handleSignIn(@RequestBody UserCredentialsDTO userCredentialsDTO) {
        // should be hashed password
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userCredentialsDTO.getUsername(), userCredentialsDTO.getPassword());
        authenticationManager.authenticate(authToken);
        String token = jwtUtil.generateToken(userCredentialsDTO.getUsername());
        return new TokenResponseDTO(token);
    }

    @GetMapping("/test")
    public String handleTest() {
        UserDetails userDetails = getUserDetailsFromAuthentication();
        return "This should be secured, username is " + userDetails.getUsername();
    }

    @GetMapping("/scope_admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String handleScopeAdmin() {
        return getUserDetailsFromAuthentication().getUsername() + " has admin rights!";
    }


}
