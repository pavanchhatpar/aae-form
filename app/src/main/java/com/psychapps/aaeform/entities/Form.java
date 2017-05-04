package com.psychapps.aaeform.entities;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by pavan on 05/05/2017.
 */

@IgnoreExtraProperties
public class Form {
    public Form(){
        //for firebase
    }
    public boolean referredToSpl;
    public String emailid;
    public String[] responses;

    public boolean isReferredToSpl() {
        return referredToSpl;
    }

    public String getEmailid() {
        return emailid;
    }

    public String[] getResponses() {
        return responses;
    }
}
