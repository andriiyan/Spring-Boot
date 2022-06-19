package com.andriiyan.springlearning.springboot.impl.controllers;

import com.andriiyan.springlearning.springboot.api.dao.UserDao;
import com.andriiyan.springlearning.springboot.impl.model.UserEntity;
import com.andriiyan.springlearning.springboot.security.JwtUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest
public class AdminControllerTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity(springSecurityFilterChain))
                .build();
        userDao.deleteAll();
    }

    @Test
    public void me_should_return_403_for_non_authenticated() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/user/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void me_should_return_current_user() throws Exception {
        final String password = "pass";
        UserEntity savedUser = userDao.save(new UserEntity("Andrii2", "andrii.email@a.com", passwordEncoder.encode(password), "ADMIN"));
        String token = jwtUtil.generateToken(savedUser.getName());
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/user/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.name").value(savedUser.getName()))
                .andExpect(jsonPath("$.email").value(savedUser.getEmail()))
                .andExpect(jsonPath("$.password").value(savedUser.getPassword()))
                .andExpect(jsonPath("$.scope").value(savedUser.getScope()));
    }

}
