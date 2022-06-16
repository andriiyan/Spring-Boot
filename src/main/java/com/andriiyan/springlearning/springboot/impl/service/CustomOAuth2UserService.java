package com.andriiyan.springlearning.springboot.impl.service;

import com.andriiyan.springlearning.springboot.api.dao.UserDao;
import com.andriiyan.springlearning.springboot.impl.model.UserDetailsImpl;
import com.andriiyan.springlearning.springboot.impl.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @NonNull
    private final UserDao userDao;

    @Autowired
    public CustomOAuth2UserService(@NonNull UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Optional<UserEntity> optionalUserEntity = userDao.findByName(oAuth2User.getName());
        UserEntity user;
        if (optionalUserEntity.isEmpty()) {
            String email = oAuth2User.getAttribute("email");
            if (email == null) {
                email = oAuth2User.getAttribute("login");
            }
            if (email == null) {
                email = oAuth2User.getName();
            }
            user = userDao.save(new UserEntity(oAuth2User.getName(), email, ""));
        } else {
            user = optionalUserEntity.get();
        }
        return new UserDetailsImpl(user);
    }
}
