package com.psychapps.aaeform.entities;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by pavan on 04/05/2017.
 */
@IgnoreExtraProperties
public class User {

    public String name;
    public String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User() {
        //for firebase
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


}
