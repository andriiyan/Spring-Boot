package com.andriiyan.springlearning.springboot.impl.facade;

import com.andriiyan.springlearning.springboot.api.exceptions.NotEnoughMoneyException;
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
import com.andriiyan.springlearning.springboot.impl.model.UserAccountEntity;
import com.andriiyan.springlearning.springboot.impl.model.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(MockitoJUnitRunner.class)
public class BookingFacadeImplTest {

    @Mock
    private EventService eventService;
    @Mock
    private TicketService ticketService;
    @Mock
    private UserService userService;
    @Mock
    private UserAccountService userAccountService;

    @InjectMocks
    private BookingFacadeImpl bookingFacade;

    /**
     * To make sure implementation not tied to some particular values.
     */
    private final Random random = new Random(System.currentTimeMillis());

    @Test
    public void getEventById_should_be_delegated() {
        EventEntity returningEvent = Mockito.mock(EventEntity.class);
        long eventId = random.nextLong();

        Mockito.when(eventService.getEventById(eventId)).thenReturn(returningEvent);

        Event facadeEvent = bookingFacade.getEventById(eventId);
        Assert.assertEquals(returningEvent, facadeEvent);

        Mockito.verify(eventService).getEventById(eventId);
        //Mockito.verify(bookingFacade).getEventById(eventId);
    }

    @Test
    public void getEventsByTitle_should_be_delegated() {
        EventEntity event = Mockito.mock(EventEntity.class);
        String title = String.valueOf(random.nextInt());
        int pageSize = random.nextInt();
        int pageNum = random.nextInt();
        List<EventEntity> returningEvents = List.of(event);

        Mockito.when(eventService.getEventsByTitle(title, pageSize, pageNum)).thenReturn(returningEvents);

        List<EventEntity> facadeEvents = bookingFacade.getEventsByTitle(title, pageSize, pageNum);
        Assert.assertEquals(returningEvents, facadeEvents);

        Mockito.verify(eventService).getEventsByTitle(title, pageSize, pageNum);
        //Mockito.verify(bookingFacade).getEventsByTitle(title, pageSize, pageNum);
    }

    @Test
    public void getEventsForDay_should_be_delegated() {
        EventEntity event = Mockito.mock(EventEntity.class);
        Date date = new Date(random.nextLong());
        int pageSize = random.nextInt();
        int pageNum = random.nextInt();
        List<EventEntity> returningEvents = List.of(event);

        Mockito.when(eventService.getEventsForDay(date, pageSize, pageNum)).thenReturn(returningEvents);

        List<EventEntity> facadeEvents = bookingFacade.getEventsForDay(date, pageSize, pageNum);
        Assert.assertEquals(returningEvents, facadeEvents);

        Mockito.verify(eventService).getEventsForDay(date, pageSize, pageNum);
    }

    @Test
    public void createEvent_should_be_delegated() {
        EventEntity returningEvent = Mockito.mock(EventEntity.class);
        EventEntity creatingEvent = Mockito.mock(EventEntity.class);
        Mockito.when(eventService.createEvent(creatingEvent)).thenReturn(returningEvent);

        Event facadeEvent = bookingFacade.createEvent(creatingEvent);
        Assert.assertEquals(returningEvent, facadeEvent);

        Mockito.verify(eventService).createEvent(creatingEvent);
    }

    @Test
    public void updateEvent_should_be_delegated()  {
        EventEntity returningEvent = Mockito.mock(EventEntity.class);
        EventEntity updatingEvent = Mockito.mock(EventEntity.class);
        Mockito.when(eventService.updateEvent(updatingEvent)).thenReturn(returningEvent);

        Event facadeEvent = bookingFacade.updateEvent(updatingEvent);
        Assert.assertEquals(returningEvent, facadeEvent);

        Mockito.verify(eventService).updateEvent(updatingEvent);
    }

    @Test
    public void deleteEvent_should_be_delegated() {
        long deletingEventId = random.nextLong();
        Mockito.when(eventService.deleteEvent(deletingEventId)).thenReturn(true);

        boolean facadeResult = bookingFacade.deleteEvent(deletingEventId);
        Assert.assertTrue(facadeResult);

        Mockito.verify(eventService).deleteEvent(deletingEventId);
    }

    @Test
    public void getUserById_should_be_delegated() {
        User returningUser = Mockito.mock(User.class);
        long userId = random.nextLong();
        Mockito.when(userService.getUserById(userId)).thenReturn(returningUser);

        User facadeUser = bookingFacade.getUserById(userId);
        Assert.assertEquals(returningUser, facadeUser);

        Mockito.verify(userService).getUserById(userId);
    }

    @Test
    public void getUserByEmail_should_be_delegated() {
        User returningUser = Mockito.mock(User.class);
        String userEmail = String.valueOf(random.nextLong());
        Mockito.when(userService.getUserByEmail(userEmail)).thenReturn(returningUser);

        User facadeUser = bookingFacade.getUserByEmail(userEmail);
        Assert.assertEquals(returningUser, facadeUser);

        Mockito.verify(userService).getUserByEmail(userEmail);
    }

    @Test
    public void getUsersByName_should_be_delegated() {
        List<UserEntity> returningUsers = List.of(Mockito.mock(UserEntity.class));
        String userName = String.valueOf(random.nextLong());
        int pageSize = random.nextInt();
        int pageNum = random.nextInt();
        Mockito.when(userService.getUsersByName(userName, pageSize, pageNum)).thenReturn(returningUsers);

        List<UserEntity> facadeUsers = bookingFacade.getUsersByName(userName, pageSize, pageNum);
        Assert.assertEquals(returningUsers, facadeUsers);

        Mockito.verify(userService).getUsersByName(userName, pageSize, pageNum);
    }


    @Test
    public void createUser_should_be_delegated() {
        User returningUser = Mockito.mock(User.class);
        UserEntity creatingUser = Mockito.mock(UserEntity.class);
        Mockito.when(userService.createUser(creatingUser)).thenReturn(returningUser);

        User facadeUser = bookingFacade.createUser(creatingUser);
        Assert.assertEquals(returningUser, facadeUser);

        Mockito.verify(userService).createUser(creatingUser);
    }

    @Test
    public void updateUser_should_be_delegated() {
        User returningUser = Mockito.mock(User.class);
        UserEntity updatingUser = Mockito.mock(UserEntity.class);
        Mockito.when(userService.updateUser(updatingUser)).thenReturn(returningUser);

        User facadeUser = bookingFacade.updateUser(updatingUser);
        Assert.assertEquals(returningUser, facadeUser);

        Mockito.verify(userService).updateUser(updatingUser);
    }

    @Test
    public void deleteUser_should_be_delegated() {
        long deletingUserId = random.nextLong();
        Mockito.when(userService.deleteUser(deletingUserId)).thenReturn(true);

        boolean facadeResult = bookingFacade.deleteUser(deletingUserId);
        Assert.assertTrue(facadeResult);

        Mockito.verify(userService).deleteUser(deletingUserId);
    }

    @Test
    public void bookTicket_should_be_delegated() throws NotEnoughMoneyException {
        long userId = random.nextLong();
        long eventId = random.nextLong();
        int place = random.nextInt();
        Ticket returningTicket = Mockito.mock(Ticket.class);
        EventEntity event = new EventEntity(eventId, "a", new Date(), 10);
        UserAccount userAccount = new UserAccountEntity(userId, 100);

        Mockito.when(eventService.getEventById(eventId)).thenReturn(event);
        Mockito.when(userAccountService.getUserAmount(userId)).thenReturn(userAccount);
        Mockito.when(ticketService.bookTicket(userId, eventId, place, Ticket.Category.BAR)).thenReturn(returningTicket);

        Ticket facadeTicket = bookingFacade.bookTicket(userId, eventId, place, Ticket.Category.BAR);
        Assert.assertEquals(returningTicket, facadeTicket);

        Mockito.verify(ticketService).bookTicket(userId, eventId, place, Ticket.Category.BAR);
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void bookTicket_throw_NotEnoughMoneyException() throws NotEnoughMoneyException {
        long userId = random.nextLong();
        long eventId = random.nextLong();
        int place = random.nextInt();
        Ticket returningTicket = Mockito.mock(Ticket.class);
        EventEntity event = new EventEntity(eventId, "a", new Date(), 1000);
        UserAccount userAccount = new UserAccountEntity(userId, event.getTicketPrice() - 10);

        Mockito.when(eventService.getEventById(eventId)).thenReturn(event);
        Mockito.when(userAccountService.getUserAmount(userId)).thenReturn(userAccount);

        Ticket facadeTicket = bookingFacade.bookTicket(userId, eventId, place, Ticket.Category.BAR);
        Assert.assertEquals(returningTicket, facadeTicket);

        Mockito.verify(ticketService).bookTicket(userId, eventId, place, Ticket.Category.BAR);
    }

    @Test
    public void getBookedTickets_should_be_delegated() {
        User user = Mockito.mock(User.class);
        int pageSize = random.nextInt();
        int pageNum = random.nextInt();
        List<TicketEntity> returningTickets = List.of(Mockito.mock(TicketEntity.class));

        Mockito.when(ticketService.getBookedTickets(user, pageSize, pageNum)).thenReturn(returningTickets);

        List<TicketEntity> facadeTickets = bookingFacade.getBookedTickets(user, pageSize, pageNum);
        Assert.assertEquals(returningTickets, facadeTickets);

        Mockito.verify(ticketService).getBookedTickets(user, pageSize, pageNum);
    }

    @Test
    public void getBookedTicketsEvent_should_be_delegated() {
        EventEntity event = Mockito.mock(EventEntity.class);
        int pageSize = random.nextInt();
        int pageNum = random.nextInt();
        List<TicketEntity> returningTickets = List.of(Mockito.mock(TicketEntity.class));

        Mockito.when(ticketService.getBookedTickets(event, pageSize, pageNum)).thenReturn(returningTickets);

        List<TicketEntity> facadeTickets = bookingFacade.getBookedTickets(event, pageSize, pageNum);
        Assert.assertEquals(returningTickets, facadeTickets);

        Mockito.verify(ticketService).getBookedTickets(event, pageSize, pageNum);
    }

    @Test
    public void cancelTicket_should_be_delegated() {
        long ticketId = random.nextLong();

        Mockito.when(ticketService.cancelTicket(ticketId)).thenReturn(true);

        boolean facadeResult = bookingFacade.cancelTicket(ticketId);
        Assert.assertTrue(facadeResult);

        Mockito.verify(ticketService).cancelTicket(ticketId);
    }

}