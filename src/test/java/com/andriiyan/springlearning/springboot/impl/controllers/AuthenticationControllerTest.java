package com.andriiyan.springlearning.springboot.impl.controllers;

import com.andriiyan.springlearning.springboot.api.dao.UserDao;
import com.andriiyan.springlearning.springboot.impl.model.UserEntity;
import com.andriiyan.springlearning.springboot.impl.model.transport.UserCredentialsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
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
public class AuthenticationControllerTest {

    @Autowired
    private UserDao userDao;

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
    public void should_return_token_on_user_login() throws Exception {
        final String password = "123456";
        UserEntity savedUser = userDao.save(new UserEntity("Andrii2", "andrii.email@a.com", passwordEncoder.encode(password), "ADMIN"));
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                .content(asJsonString(new UserCredentialsDTO(savedUser.getName(), password)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").hasJsonPath());
    }

    @Test
    public void should_return_403_if_password_is_wrong_on_user_login() throws Exception {
        final String password = "123456";
        UserEntity savedUser = userDao.save(new UserEntity("Andrii2", "andrii.email@a.com", passwordEncoder.encode(password), "ADMIN"));
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                        .content(asJsonString(new UserCredentialsDTO(savedUser.getName(), password + '2')))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void should_return_403_if_user_not_found_on_user_login() throws Exception {
        UserEntity savedUser = userDao.save(new UserEntity("Test", "andrii.email@a.com", passwordEncoder.encode("AAAA"), "ADMIN"));
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                        .content(asJsonString(new UserCredentialsDTO(savedUser.getName(), savedUser.getPassword())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "AndriiTest", password = "test")
    public void should_return_passed_token_on_logged() throws Exception {
        final String token = "12389102389128903as90sd890123klnasd";
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/logged")
                        .queryParam("token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
