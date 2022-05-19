package com.andriiyan.springlearning.springboot.impl.model;

import com.andriiyan.springlearning.springboot.api.model.Ticket;
import com.andriiyan.springlearning.springboot.impl.utils.converter.CategoryConverter;

import javax.persistence.*;
import java.io.Serial;

@Entity
@Table(
        name = "ticket",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "place"})
)
public class TicketEntity implements Ticket {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "event_id", nullable = false)
    private long eventId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "category", nullable = false)
    @Convert(converter = CategoryConverter.class)
    private Category category;

    @Column(name = "place", nullable = false)
    private int place;

    public TicketEntity() {
    }

    public TicketEntity(long eventId, long userId, Category category, int place) {
        this(0, eventId, userId, category, place);
    }

    public TicketEntity(long id, long eventId, long userId, Category category, int place) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.category = category;
        this.place = place;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getEventId() {
        return eventId;
    }

    @Override
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int getPlace() {
        return place;
    }

    @Override
    public void setPlace(int place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "TicketImpl{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", userId=" + userId +
                ", category=" + category +
                ", place=" + place +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketEntity)) return false;

        TicketEntity that = (TicketEntity) o;

        if (id != that.id) return false;
        if (eventId != that.eventId) return false;
        if (userId != that.userId) return false;
        if (place != that.place) return false;
        return category == that.category;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (eventId ^ (eventId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + place;
        return result;
    }
}
