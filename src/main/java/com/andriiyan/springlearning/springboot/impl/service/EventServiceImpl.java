package com.andriiyan.springlearning.springboot.impl.service;

import com.andriiyan.springlearning.springboot.api.dao.EventDao;
import com.andriiyan.springlearning.springboot.api.model.Event;
import com.andriiyan.springlearning.springboot.api.service.EventService;
import com.andriiyan.springlearning.springboot.impl.model.EventEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    @Autowired
    private EventDao eventDao;

    @Override
    public Event getEventById(long eventId) {
        final Event event = eventDao.findById(eventId).get();
        logger.debug("getEventById was invoked with userId={} and returning {}", eventId, event);
        return event;
    }

    @Override
    public List<EventEntity> getEventsByTitle(String title, int pageSize, int pageNum) {
        final List<EventEntity> events = eventDao.findAllByTitle(title, PageRequest.of(pageNum, pageSize));
        logger.debug("getEventsByTitle was invoked with title={}, pageSize={}, pageNum={} and returning {}", title, pageSize, pageNum, events);
        return events;
    }

    @Override
    public List<EventEntity> getEventsForDay(Date day, int pageSize, int pageNum) {
        final List<EventEntity> events = eventDao.findAllByDate(day, PageRequest.of(pageNum, pageSize));
        logger.debug("getEventsForDay was invoked with day={}, pageSize={}, pageNum={} and returning {}", day, pageSize, pageNum, events);
        return events;
    }

    @Override
    public Event createEvent(EventEntity event) {
        final Event mEvent = eventDao.save(event);
        logger.debug("createEvent was invoked with event={} and returning {}", event, mEvent);
        return mEvent;
    }

    @Override
    public Event updateEvent(EventEntity event) {
        final Event mEvent = eventDao.save(event);
        logger.debug("updateEvent was invoked with event={} and returning {}", event, mEvent);
        return mEvent;
    }

    @Override
    public boolean deleteEvent(long eventId) {
        eventDao.deleteById(eventId);
        logger.debug("deleteEvent was invoked with eventId={}", eventId);
        return true;
    }

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }
}
