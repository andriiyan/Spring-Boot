package com.andriiyan.springlearning.springboot.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String test() {
        return "ROOT";
    }

    @GetMapping("/secured")
    public String secured() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return "SECURED " + userDetails.getUsername();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
