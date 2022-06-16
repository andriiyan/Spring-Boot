package com.andriiyan.springlearning.springboot.impl.controllers;

import com.andriiyan.springlearning.springboot.impl.model.UserDetailsImpl;
import com.andriiyan.springlearning.springboot.security.SecurityConfiguration;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@SecurityRequirement(name = SecurityConfiguration.SCHEME)
public class AdminController {

    @GetMapping(value = "/test", produces = { MediaType.APPLICATION_JSON_VALUE })
    public String handleScopeAdmin(@AuthenticationPrincipal UserDetailsImpl principal) {
        return principal.getName() + " has admin rights!";
    }

}
