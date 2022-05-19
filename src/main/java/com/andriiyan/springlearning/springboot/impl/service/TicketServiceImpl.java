package com.andriiyan.springlearning.springboot.impl.service;

import com.andriiyan.springlearning.springboot.api.dao.TicketDao;
import com.andriiyan.springlearning.springboot.api.model.Event;
import com.andriiyan.springlearning.springboot.api.model.Ticket;
import com.andriiyan.springlearning.springboot.api.model.User;
import com.andriiyan.springlearning.springboot.api.service.TicketService;
import com.andriiyan.springlearning.springboot.impl.model.TicketEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class TicketServiceImpl implements TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Autowired
    private TicketDao ticketDao;

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        final Ticket ticket = ticketDao.save(new TicketEntity(0, eventId, userId, category, place));
        logger.debug("bookTicket was invoked with userId={}, eventId={}, place={}, category={} and returning {}", userId, eventId, place, category, ticket);
        return ticket;
    }

    @Override
    public List<TicketEntity> getBookedTickets(User user, int pageSize, int pageNum) {
        final List<TicketEntity> tickets = ticketDao.findAllByUserId(user.getId(), PageRequest.of(pageNum, pageSize));
        logger.debug("getBookedTickets was invoked user={}, pageSize={}, pageNum={} and returning {}", user, pageSize, pageNum, tickets);
        return tickets;
    }

    @Override
    public List<TicketEntity> getBookedTickets(Event event, int pageSize, int pageNum) {
        final List<TicketEntity> tickets = ticketDao.findAllByEventId(event.getId(), PageRequest.of(pageNum, pageSize));
        logger.debug("getBookedTickets was invoked with event={}, pageSize={}, pageNum={} and returning {}", event, pageSize, pageNum, tickets);
        return tickets;
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        ticketDao.deleteById(ticketId);
        logger.debug("cancelTicket was invoked with ticketId={}", ticketId);
        return true;
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }
}
