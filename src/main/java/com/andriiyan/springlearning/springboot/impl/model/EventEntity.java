package com.andriiyan.springlearning.springboot.impl.model;

import com.andriiyan.springlearning.springboot.api.model.Event;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serial;
import java.util.Date;

@Entity
@Table(
        name = "event",
        uniqueConstraints = @UniqueConstraint(columnNames = {"title", "date"})
)
public class EventEntity implements Event {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "date", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date date;

    @Column(name = "time", nullable = false)
    @Temporal(value = TemporalType.TIME)
    private Date time;

    @Column(name = "ticket_price", nullable = false)
    private double ticketPrice;

    public EventEntity(@NonNull String title, @NonNull Date dateAndTime, double ticketPrice) {
        this(0, title, dateAndTime, ticketPrice);
    }

    public EventEntity(long id, @NonNull String title, @NonNull Date dateAndTime, double ticketPrice) {
        this.id = id;
        this.title = title;
        this.ticketPrice = ticketPrice;
        setDate(dateAndTime);
    }

    public EventEntity() {
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
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
        this.time = date;
    }

    @Override
    public double getTicketPrice() {
        return ticketPrice;
    }

    @Override
    public void setTicketPrice(double price) {
        this.ticketPrice = price;
    }

    @Override
    public String toString() {
        return "EventImpl{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventEntity)) return false;

        EventEntity that = (EventEntity) o;

        if (id != that.id) return false;
        if (Double.compare(that.ticketPrice, ticketPrice) != 0) return false;
        if (!title.equals(that.title)) return false;
        if (!date.equals(that.date)) return false;
        return time.equals(that.time);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + title.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + time.hashCode();
        temp = Double.doubleToLongBits(ticketPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
