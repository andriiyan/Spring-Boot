package com.andriiyan.springlearning.springboot.impl.service;

import com.andriiyan.springlearning.springboot.api.dao.UserDao;
import com.andriiyan.springlearning.springboot.api.model.User;
import com.andriiyan.springlearning.springboot.impl.model.UserDetailsImpl;
import com.andriiyan.springlearning.springboot.impl.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @NonNull
    private final UserDao userDao;

    @Autowired
    public CustomUserDetailsService(@NonNull UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> foundedUser = userDao.findByName(username);
        if (foundedUser.isEmpty()) {
            throw new UsernameNotFoundException("User with name " + username + " not found in the database!");
        }
        return new UserDetailsImpl(foundedUser.get());
    }
}
