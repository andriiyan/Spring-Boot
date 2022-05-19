package com.andriiyan.springlearning.springboot.impl.facade;

import com.andriiyan.springlearning.springboot.api.exceptions.NotEnoughMoneyException;
import com.andriiyan.springlearning.springboot.api.facade.BookingFacade;
import com.andriiyan.springlearning.springboot.api.model.Event;
import com.andriiyan.springlearning.springboot.api.model.Ticket;
import com.andriiyan.springlearning.springboot.api.model.User;
import com.andriiyan.springlearning.springboot.api.model.UserAccount;
import com.andriiyan.springlearning.springboot.api.service.EventService;
import com.andriiyan.springlearning.springboot.api.service.TicketService;
import com.andriiyan.springlearning.springboot.api.service.UserAccountService;
import com.andriiyan.springlearning.springboot.api.service.UserService;
import com.andriiyan.springlearning.springboot.impl.model.EventEntity;
import com.andriiyan.springlearning.springboot.impl.model.TicketEntity;
import com.andriiyan.springlearning.springboot.impl.model.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
class BookingFacadeImpl implements BookingFacade {

    private static final Logger logger = LoggerFactory.getLogger(BookingFacadeImpl.class);

    private final EventService eventService;
    private final TicketService ticketService;
    private final UserService userService;
    private final UserAccountService userAccountService;

    @Autowired
    public BookingFacadeImpl(EventService eventService, TicketService ticketService, UserService userService, UserAccountService userAccountService) {
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.userAccountService = userAccountService;
    }

    @Override
    public Event getEventById(long eventId) {
        final Event event = eventService.getEventById(eventId);
        logger.debug("getEventById invoked with {} returning {}", eventId, event);
        return event;
    }

    @Override
    public List<EventEntity> getEventsByTitle(String title, int pageSize, int pageNum) {
        final List<EventEntity> events = eventService.getEventsByTitle(title, pageSize, pageNum);
        logger.debug("getEventsByTitle invoked with title={}, pageSize={}, pageNum={} and returned {}", title, pageSize, pageNum, events);
        return events;
    }

    @Override
    public List<EventEntity> getEventsForDay(Date day, int pageSize, int pageNum) {
        final List<EventEntity> events = eventService.getEventsForDay(day, pageSize, pageNum);
        logger.debug("getEventsByTitle invoked with day={}, pageSize={}, pageNum={} and returned {}", day, pageSize, pageNum, events);
        return events;
    }

    @Override
    public Event createEvent(EventEntity event) {
        final Event mEvent = eventService.createEvent(event);
        logger.debug("createEvent was invoked with {} and returning {}", event, mEvent);
        return mEvent;
    }

    @Override
    public Event updateEvent(EventEntity event) {
        final Event mEvent = eventService.updateEvent(event);
        logger.debug("updateEvent was invoked with {} and returning {}", event, mEvent);
        return mEvent;
    }

    @Override
    public boolean deleteEvent(long eventId) {
        final boolean result = eventService.deleteEvent(eventId);
        logger.debug("deleteEvent was invoked with {} and returning {}", eventId, result);
        return result;
    }

    @Override
    public User getUserById(long userId) {
        final User user = userService.getUserById(userId);
        logger.debug("getUserById was invoked with {} and returning {}", userId, user);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        final User user = userService.getUserByEmail(email);
        logger.debug("getUserByEmail was invoked with {} and returning {}", email, user);
        return user;
    }

    @Override
    public List<UserEntity> getUsersByName(String name, int pageSize, int pageNum) {
        final List<UserEntity> users = userService.getUsersByName(name, pageSize, pageNum);
        logger.debug("getUsersByName was invoked with name={}, pageSize={}, pageNum={} and returning {}", name, pageSize, pageNum, users);
        return users;
    }

    @Override
    public User createUser(UserEntity user) {
        final User mUser = userService.createUser(user);
        logger.debug("createUser was invoked with {} and returning {}", user, mUser);
        return mUser;
    }

    @Override
    public User updateUser(UserEntity user) {
        final User mUser = userService.updateUser(user);
        logger.debug("updateUser was invoked with {} and returning {}", user, mUser);
        return mUser;
    }

    @Override
    public boolean deleteUser(long userId) {
        final boolean result = userService.deleteUser(userId);
        logger.debug("deleteUser was invoked with {} and returning {}", userId, result);
        return result;
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) throws NotEnoughMoneyException {
        final UserAccount userAccount = userAccountService.getUserAmount(userId);
        final Event event = eventService.getEventById(eventId);
        if (userAccount.getAmount() >= event.getTicketPrice()) {
            userAccountService.refillUser(-event.getTicketPrice(), userId);
            final Ticket ticket = ticketService.bookTicket(userId, eventId, place, category);
            logger.debug("bookTicket was invoked with userId={}, eventId={}, place={}, category={} and returning {}", userId, eventId, place, category, ticket);
            return ticket;
        }
        throw new NotEnoughMoneyException(event, userAccount);
    }

    @Override
    public List<TicketEntity> getBookedTickets(User user, int pageSize, int pageNum) {
        final List<TicketEntity> tickets = ticketService.getBookedTickets(user, pageSize, pageNum);
        logger.debug("getBookedTickets was invoked with user={}, pageSize={}, pageNum={} and returning {}", user, pageSize, pageNum, tickets);
        return tickets;
    }

    @Override
    public List<TicketEntity> getBookedTickets(Event event, int pageSize, int pageNum) {
        final List<TicketEntity> tickets = ticketService.getBookedTickets(event, pageSize, pageNum);
        logger.debug("getBookedTickets was invoked with event={}, pageSize={}, pageNum={} and returning {}", event, pageSize, pageNum, tickets);
        return tickets;
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        final boolean result = ticketService.cancelTicket(ticketId);
        logger.debug("cancelTicket was invoked with {} and returning {}", ticketId, result);
        return result;
    }

    @Override
    public UserAccount refillUser(double amount, long userId) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount should be > 0");
        }
        UserAccount result = userAccountService.refillUser(amount, userId);
        logger.debug("refillUser was invoked with the amount: {}, userId: {} and returned {}", amount, userId, result);
        return result;
    }

    @Override
    public UserAccount getUserAmount(long userId) {
        UserAccount result = userAccountService.getUserAmount(userId);
        logger.debug("getUserAmount was invoked with the userId: {} and returned {}", userId, result);
        return result;
    }

}
