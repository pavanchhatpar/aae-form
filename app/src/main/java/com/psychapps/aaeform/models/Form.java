package com.psychapps.aaeform.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by pavan on 05/05/2017.
 */

@IgnoreExtraProperties
public class Form {
    public Form(){
        //for firebase
    }
    public Form(String emailid) {
        this.emailid = emailid;
        this.responses = new ArrayList<String>();
    }
    public boolean referredToSpl;
    public String emailid;
    public ArrayList<String> responses;

    public boolean isReferredToSpl() {
        return referredToSpl;
    }

    public String getEmailid() {
        return emailid;
    }

    public ArrayList<String> getResponses() {
        return responses;
    }

    public void setReferredToSpl(boolean referredToSpl) {
        this.referredToSpl = referredToSpl;
    }

    public void setResponses(ArrayList<String> responses) {
        this.responses = responses;
    }
}
