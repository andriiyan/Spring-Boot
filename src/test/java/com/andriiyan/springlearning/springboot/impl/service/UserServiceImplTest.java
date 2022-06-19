package com.andriiyan.springlearning.springboot.impl.service;

import com.andriiyan.springlearning.springboot.api.dao.UserDao;
import com.andriiyan.springlearning.springboot.api.model.User;
import com.andriiyan.springlearning.springboot.impl.model.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void getUserById() {
        final long userId = 123;
        final Optional<UserEntity> returningUser = Optional.of(new UserEntity("name", "email", "pass"));
        Mockito.when(userDao.findById(userId)).thenReturn(returningUser);

        final User returnedUser = userService.getUserById(userId);
        Assert.assertEquals(returningUser.get(), returnedUser);
        Mockito.verify(userDao).findById(userId);
    }

    @Test
    public void getUserByEmail() {
        final String email = "email@test.com";
        UserEntity returningUser = new UserEntity("name", "email", "pass");
        returningUser.setEmail(email);
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.of(returningUser));

        final User returnedUser1 = userService.getUserByEmail(email);

        Assert.assertEquals(returningUser, returnedUser1);
        Mockito.verify(userDao).findByEmail(email);
    }

    @Test
    public void getUsersByName() {
        final String searchingName = "name";
        int pageSize = 3;
        int pageNum = 2;

        List<UserEntity> returningUsers = new ArrayList<>();
        for (int i = 0; i < (pageNum + 1) * pageSize; i++) {
            returningUsers.add(new UserEntity("name " + i, "email " + i, "pass"));
        }

        Mockito.when(userDao.findAllByName(searchingName, PageRequest.of(pageNum, pageSize))).thenReturn(returningUsers);

        List<UserEntity> returnedUsers = userService.getUsersByName(searchingName, pageSize, pageNum);

        Assert.assertEquals(returningUsers, returnedUsers);
        Mockito.verify(userDao).findAllByName(searchingName, PageRequest.of(pageNum, pageSize));

        // case when there are no users that contains that name
        returningUsers = Collections.emptyList();
        Mockito.when(userDao.findAllByName(searchingName, PageRequest.of(pageNum, pageSize))).thenReturn(returningUsers);

        returnedUsers = userService.getUsersByName(searchingName, pageSize, pageNum);
        Assert.assertEquals(returningUsers, returnedUsers);
        Mockito.verify(userDao, Mockito.times(2)).findAllByName(searchingName, PageRequest.of(pageNum, pageSize));
    }

    @Test
    public void createUser() {
        final UserEntity user = new UserEntity("name", "email", "pass");
        final UserEntity returningUser = new UserEntity("name1", "email1", "pass");
        Mockito.when(userDao.save(user)).thenReturn(returningUser);

        final User returnedUser = userService.createUser(user);

        Assert.assertEquals(returningUser, returnedUser);
        Mockito.verify(userDao).save(user);
    }

    @Test
    public void updateUser() {
        final UserEntity user = new UserEntity("name", "email", "pass");
        final UserEntity returningUser = new UserEntity("name1", "email1", "pass");
        Mockito.when(userDao.save(user)).thenReturn(returningUser);

        final User returnedUser = userService.updateUser(user);

        Assert.assertEquals(returningUser, returnedUser);
        Mockito.verify(userDao).save(user);
    }

    @Test
    public void deleteUser() {
        long userId = 123;
        Mockito.doNothing().when(userDao).deleteById(userId);

        final boolean result = userService.deleteUser(userId);

        Assert.assertTrue(result);
        Mockito.verify(userDao).deleteById(userId);
    }
}