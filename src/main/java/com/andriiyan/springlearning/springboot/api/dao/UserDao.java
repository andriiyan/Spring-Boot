package com.andriiyan.springlearning.springboot.api.dao;

import com.andriiyan.springlearning.springboot.impl.model.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends PagingAndSortingRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByName(String name);

    List<UserEntity> findAllByName(String name, Pageable pageable);

}
