package com.andriiyan.springlearning.springboot.impl.service;

import com.andriiyan.springlearning.springboot.api.dao.TicketDao;
import com.andriiyan.springlearning.springboot.api.model.Event;
import com.andriiyan.springlearning.springboot.api.model.Ticket;
import com.andriiyan.springlearning.springboot.api.model.User;
import com.andriiyan.springlearning.springboot.impl.model.EventEntity;
import com.andriiyan.springlearning.springboot.impl.model.TicketEntity;
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
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceImplTest {

    @Mock
    private TicketDao ticketDao;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    public void bookTicket_shouldInvokeDao() {
        final Ticket returningTicket = new TicketEntity(1, 1, Ticket.Category.BAR, 2);
        final long userId = 1;
        final long eventId = 1;
        final int place = 12;
        final Ticket.Category category = Ticket.Category.PREMIUM;
        Mockito.when(ticketDao.save(Mockito.any())).thenReturn(returningTicket);

        final Ticket returnedTicket = ticketService.bookTicket(userId, eventId, place, category);

        Assert.assertEquals(returningTicket, returnedTicket);
        Mockito.verify(ticketDao).save(Mockito.any());
    }

    @Test
    public void getBookedTicketsUser_shouldReturnCorrectTickets() {
        final int pageSize = 5;
        final int pageNum = 2;

        final User user = new UserEntity("name", "email", "pass");
        final List<TicketEntity> allTickets = new ArrayList<>();
        for (int i = 0; i < pageSize * (pageNum + 1); i++) {
            allTickets.add(new TicketEntity(i, i, Ticket.Category.BAR, i));
        }        Mockito.when(ticketDao.findAllByUserId(user.getId(), PageRequest.of(pageNum, pageSize))).thenReturn(allTickets);

        final List<TicketEntity> returnedTickets = ticketService.getBookedTickets(user, pageSize, pageNum);

        Assert.assertEquals(allTickets, returnedTickets);
        Mockito.verify(ticketDao).findAllByUserId(user.getId(), PageRequest.of(pageNum, pageSize));
    }

    @Test
    public void getBookedTicketsEvent_shouldReturnCorrectTickets() {
        final int pageSize = 5;
        final int pageNum = 2;

        final Event event = new EventEntity("test", new Date(), 20);
        final List<TicketEntity> allTickets = new ArrayList<>();
        for (int i = 0; i < pageSize * (pageNum + 1); i++) {
            allTickets.add(new TicketEntity(i, i, Ticket.Category.BAR, i));
        }
        Mockito.when(ticketDao.findAllByEventId(event.getId(), PageRequest.of(pageNum, pageSize))).thenReturn(allTickets);

        final List<TicketEntity> returnedTickets = ticketService.getBookedTickets(event, pageSize, pageNum);

        Assert.assertEquals(allTickets, returnedTickets);
        Mockito.verify(ticketDao).findAllByEventId(event.getId(), PageRequest.of(pageNum, pageSize));
    }


    @Test
    public void cancelTicket_shouldReturnSameModelAsDao() {
        final long ticketId = 100;
        Mockito.doNothing().when(ticketDao).deleteById(ticketId);

        Assert.assertTrue(ticketService.cancelTicket(ticketId));
        Mockito.verify(ticketDao).deleteById(ticketId);
    }

}