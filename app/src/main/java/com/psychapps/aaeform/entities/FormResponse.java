package com.psychapps.aaeform.entities;

/**
 * Created by pavan on 05/05/2017.
 */

public class FormResponse {
    public String form;
    public String section;
    public int no;
    public int[] min, mod, max;
    public FormResponse() {

    }

    public String getForm() {
        return form;
    }

    public String getSection() {
        return section;
    }

    public int getNo() {
        return no;
    }

    public int[] getMin() {
        return min;
    }

    public int[] getMod() {
        return mod;
    }

    public int[] getMax() {
        return max;
    }
}
