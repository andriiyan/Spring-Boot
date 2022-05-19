package com.andriiyan.springlearning.springboot.api.dao;

import com.andriiyan.springlearning.springboot.impl.model.UserAccountEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserAccountDao extends CrudRepository<UserAccountEntity, Long> {

    Optional<UserAccountEntity> findByUserId(long userId);
}
