package com.andriiyan.springlearning.springboot.api.model;

import java.io.Serializable;

/**
 * Created by maksym_govorischev on 14/03/14.
 */
public interface User extends Identifierable, Serializable {
    /**
     * User Id. UNIQUE.
     * @return User Id.
     */
    long getId();
    void setId(long id);
    String getName();
    void setName(String name);

    /**
     * User email. UNIQUE.
     * @return User email.
     */
    String getEmail();
    void setEmail(String email);
}
