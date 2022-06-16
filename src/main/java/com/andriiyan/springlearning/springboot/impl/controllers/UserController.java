package com.andriiyan.springlearning.springboot.impl.controllers;

import com.andriiyan.springlearning.springboot.impl.model.UserDetailsImpl;
import com.andriiyan.springlearning.springboot.impl.model.UserEntity;
import com.andriiyan.springlearning.springboot.security.SecurityConfiguration;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = SecurityConfiguration.SCHEME)
public class UserController {

    @GetMapping(value = "/me", produces = { MediaType.APPLICATION_JSON_VALUE })
    public UserEntity handleMe(@AuthenticationPrincipal UserDetailsImpl principal) {
        return principal.userEntity();
    }

}
