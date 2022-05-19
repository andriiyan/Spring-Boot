package com.andriiyan.springlearning.springboot.impl.model;

import com.andriiyan.springlearning.springboot.api.model.UserAccount;

import javax.persistence.*;
import java.io.Serial;

@Entity
@Table(name = "user_account")
public class UserAccountEntity implements UserAccount {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "amount", nullable = false)
    private double amount;

    public UserAccountEntity() {}

    public UserAccountEntity(long id, long userId, double amount) {
        this.amount = amount;
        this.id = id;
        this.userId = userId;
    }

    public UserAccountEntity(long userId, double amount) {
        this(0, userId, amount);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void add(double amount) {
        this.amount += amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccountEntity)) return false;

        UserAccountEntity that = (UserAccountEntity) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        return Double.compare(that.amount, amount) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
