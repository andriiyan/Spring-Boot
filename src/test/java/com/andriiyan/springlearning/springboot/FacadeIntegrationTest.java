package com.andriiyan.springlearning.springboot;

import com.andriiyan.springlearning.springboot.api.dao.TicketDao;
import com.andriiyan.springlearning.springboot.api.dao.UserDao;
import com.andriiyan.springlearning.springboot.api.exceptions.NotEnoughMoneyException;
import com.andriiyan.springlearning.springboot.api.facade.BookingFacade;
import com.andriiyan.springlearning.springboot.api.model.Event;
import com.andriiyan.springlearning.springboot.api.model.Ticket;
import com.andriiyan.springlearning.springboot.api.model.User;
import com.andriiyan.springlearning.springboot.impl.model.EventEntity;
import com.andriiyan.springlearning.springboot.impl.model.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

@SpringBootTest
@ActiveProfiles("local")
public class FacadeIntegrationTest {

    @Autowired
    private BookingFacade bookingFacade;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TicketDao ticketDao;

    @Test
    public void refill_book_ticket_check_amount() throws NotEnoughMoneyException {
        final User user = bookingFacade.createUser(new UserEntity("Andrii", "Test", "pass"));
        final Event event = bookingFacade.createEvent(new EventEntity("Event", new Date(), 100));
        bookingFacade.refillUser(event.getTicketPrice() + 10, user.getId());
        final Ticket ticket = bookingFacade.bookTicket(user.getId(), event.getId(), 4, Ticket.Category.PREMIUM);
        Assert.assertEquals(user, userDao.findById(user.getId()).get());
        Assert.assertEquals(ticket, ticketDao.findById(ticket.getId()).get());
        Assert.assertEquals(ticket, bookingFacade.getBookedTickets(user, 1, 0).get(0));
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void should_throw_an_exception_if_user_has_no_enough_money() throws NotEnoughMoneyException {
        final User user = bookingFacade.createUser(new UserEntity("Andrii", "Test", "pass"));
        final Event event = bookingFacade.createEvent(new EventEntity("Event", new Date(), 100));
        bookingFacade.refillUser(event.getTicketPrice() - 10, user.getId());
        final Ticket ticket = bookingFacade.bookTicket(user.getId(), event.getId(), 4, Ticket.Category.PREMIUM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void refill_for_negative_amount_should_throw_an_exception() {
        final User user = bookingFacade.createUser(new UserEntity("Andrii", "Test", "pass"));
        bookingFacade.refillUser(-500, user.getId());
    }

}
