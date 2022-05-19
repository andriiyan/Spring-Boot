package com.andriiyan.springlearning.springboot.api.exceptions;

import com.andriiyan.springlearning.springboot.api.model.Event;
import com.andriiyan.springlearning.springboot.api.model.UserAccount;
import org.springframework.lang.NonNull;

public class NotEnoughMoneyException extends Exception {

    public NotEnoughMoneyException(@NonNull Event event, @NonNull UserAccount userAccount) {
        super("Not enough money to perform an operation! User has " + userAccount.getAmount() + ", but required " + event.getTicketPrice());
    }
}
