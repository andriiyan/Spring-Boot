package com.andriiyan.springlearning.springboot.api.service;

import com.andriiyan.springlearning.springboot.api.model.Event;
import com.andriiyan.springlearning.springboot.impl.model.EventEntity;

import java.util.Date;
import java.util.List;

public interface EventService {
    /**
     * Gets event by its id.
     * @return Event.
     */
    Event getEventById(long eventId);

    /**
     * Get list of events by matching title. Title is matched using 'contains' approach.
     * In case nothing was found, empty list is returned.
     * @param title Event title or it's part.
     * @param pageSize Pagination param. Number of events to return on a page.
     * @param pageNum Pagination param. Number of the page to return. Starts from 1.
     * @return List of events.
     */
    List<EventEntity> getEventsByTitle(String title, int pageSize, int pageNum);

    /**
     * Get list of events for specified day.
     * In case nothing was found, empty list is returned.
     * @param day Date object from which day information is extracted.
     * @param pageSize Pagination param. Number of events to return on a page.
     * @param pageNum Pagination param. Number of the page to return. Starts from 1.
     * @return List of events.
     */
    List<EventEntity> getEventsForDay(Date day, int pageSize, int pageNum);

    /**
     * Creates new event. Event id should be auto-generated.
     * @param event Event data.
     * @return Created Event object.
     */
    Event createEvent(EventEntity event);

    /**
     * Updates event using given data.
     * @param event Event data for update. Should have id set.
     * @return Updated Event object.
     */
    Event updateEvent(EventEntity event);

    /**
     * Deletes event by its id.
     * @param eventId Event id.
     * @return Flag that shows whether event has been deleted.
     */
    boolean deleteEvent(long eventId);
}
