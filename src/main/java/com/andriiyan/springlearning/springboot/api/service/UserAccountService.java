package com.andriiyan.springlearning.springboot.api.service;

import com.andriiyan.springlearning.springboot.api.model.UserAccount;

public interface UserAccountService {

    /**
     * Refills user's account.
     * @param amount amount that wil be added to the user's account.
     * @param userId id of the user.
     * @return new user's account amount.
     */
    UserAccount refillUser(double amount, long userId);

    /**
     * @param userId id of the user.
     * @return user's amount.
     */
    UserAccount getUserAmount(long userId);

}
