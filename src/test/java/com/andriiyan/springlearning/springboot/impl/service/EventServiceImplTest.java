package com.andriiyan.springlearning.springboot.impl.service;


import com.andriiyan.springlearning.springboot.api.dao.EventDao;
import com.andriiyan.springlearning.springboot.api.model.Event;
import com.andriiyan.springlearning.springboot.impl.model.EventEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceImplTest {

    @Mock
    private EventDao eventDao;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    public void getEventById_shouldReturnSameModelAsDao() {
        final long eventId = 100;
        final Optional<EventEntity> returningEvent = Optional.of(new EventEntity());
        Mockito.when(eventDao.findById(eventId)).thenReturn(returningEvent);

        final Event returnedEvent = eventService.getEventById(eventId);
        Assert.assertEquals(returningEvent.get(), returnedEvent);
        Mockito.verify(eventDao).findById(eventId);
    }

    @Test
    public void getEventsByTitle_shouldReturnCorrectAnswer() {
        final String title = "test title";
        final int pageSize = 3;
        final int pageNum = 1;

        // generating events for the 5 pages
        final List<EventEntity> returningEvents = new ArrayList<>();
        for (int i = 0; i < 5 * pageSize; i++) {
            returningEvents.add(new EventEntity("name " + i, new Date(), i));
        }

        Mockito.when(eventDao.findAllByTitle(title, PageRequest.of(pageNum, pageSize))).thenReturn(returningEvents);
        final List<EventEntity> returnedEvents = eventService.getEventsByTitle(title, pageSize, pageNum);

        Assert.assertEquals(returningEvents, returnedEvents);

        Mockito.verify(eventDao).findAllByTitle(title, PageRequest.of(pageNum, pageSize));
    }

    @Test
    public void getEventsForDay_shouldReturnCorrectAnswer() {
        final Date date = new Date() ;
        final int pageSize = 3;
        final int pageNum = 1;

        // generating events for the 5 pages
        final List<EventEntity> returningEvents = new ArrayList<>();
        for (int i = 0; i < 5 * pageSize; i++) {
            returningEvents.add(new EventEntity("name " + i, new Date(), i));
        }

        Mockito.when(eventDao.findAllByDate(date, PageRequest.of(pageNum, pageSize))).thenReturn(returningEvents);
        final List<EventEntity> returnedEvents = eventService.getEventsForDay(date, pageSize, pageNum);

        Assert.assertEquals(returningEvents, returnedEvents);

        Mockito.verify(eventDao).findAllByDate(date, PageRequest.of(pageNum, pageSize));
    }

    @Test
    public void createEvent_shouldReturnSameModelAsDao() {
        final EventEntity savingEvent = new EventEntity("name", new Date(), 100);
        final EventEntity returningEvent = new EventEntity("test", new Date(), 50);
        Mockito.when(eventDao.save(savingEvent)).thenReturn(returningEvent);

        final Event returnedEvent = eventService.createEvent(savingEvent);
        Assert.assertEquals(returningEvent, returnedEvent);
        Mockito.verify(eventDao).save(savingEvent);
    }

    @Test
    public void updateEvent_shouldReturnSameModelAsDao() {
        final EventEntity savingEvent = new EventEntity("name", new Date(), 100);
        final EventEntity returningEvent = new EventEntity("test", new Date(), 50);
        Mockito.when(eventDao.save(savingEvent)).thenReturn(returningEvent);

        final Event returnedEvent = eventService.updateEvent(savingEvent);
        Assert.assertEquals(returningEvent, returnedEvent);
        Mockito.verify(eventDao).save(savingEvent);
    }

    @Test
    public void delete_shouldReturnSameModelAsDao() {
        final long eventId = 100;
        Mockito.doNothing().when(eventDao).deleteById(eventId);

        Assert.assertTrue(eventService.deleteEvent(eventId));
        Mockito.verify(eventDao).deleteById(eventId);
    }

}