package com.psychapps.aaeform.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by pavan on 05/05/2017.
 */
@IgnoreExtraProperties
public class FormSkeleton {
    public String criteria_and_subcriteria;
    public ArrayList<String> minimal_difficulty, moderate_difficulty, high_difficulty;
    public FormSkeleton() {
        //for firebase
    }

    public String getCriteria_and_subcriteria() {
        return criteria_and_subcriteria;
    }

    public ArrayList<String> getMinimal_difficulty() {
        return minimal_difficulty;
    }

    public ArrayList<String> getModerate_difficulty() {
        return moderate_difficulty;
    }

    public ArrayList<String> getHigh_difficulty() {
        return high_difficulty;
    }
}
