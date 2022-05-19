package com.andriiyan.springlearning.springboot.api.model;

import java.io.Serializable;

public interface UserAccount extends Identifierable, Serializable {
    double getAmount();

    void setAmount(double amount);

    long getUserId();
}
