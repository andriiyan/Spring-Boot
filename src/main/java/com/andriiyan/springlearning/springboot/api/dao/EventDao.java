package com.andriiyan.springlearning.springboot.api.dao;

import com.andriiyan.springlearning.springboot.impl.model.EventEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface EventDao extends PagingAndSortingRepository<EventEntity, Long> {

    List<EventEntity> findAllByTitle(String title, Pageable pageable);

    List<EventEntity> findAllByDate(Date day, Pageable pageable);

}
