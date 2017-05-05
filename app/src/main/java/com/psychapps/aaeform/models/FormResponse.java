package com.psychapps.aaeform.models;

import java.util.ArrayList;

/**
 * Created by pavan on 05/05/2017.
 */

public class FormResponse {
    public String form;
    public String section;
    public String no;
    public ArrayList<Integer> min, mod, high;
    public FormResponse() {

    }

    public FormResponse(String section, String no, int minSize, int modSize, int highSize) {
        this.section = section;
        this.no = no;
        this.min = new ArrayList<Integer>();
        this.mod = new ArrayList<Integer>();
        this.high = new ArrayList<Integer>();
        for(int i=0; i < minSize; i++)
            this.min.add(0);
        for(int i=0; i < modSize; i++)
            this.mod.add(0);
        for(int i=0; i < highSize; i++)
            this.high.add(0);
    }

    public String getForm() {
        return form;
    }

    public String getSection() {
        return section;
    }

    public String getNo() {
        return no;
    }

    public ArrayList<Integer> getMin() {
        return min;
    }

    public ArrayList<Integer> getMod() {
        return mod;
    }

    public ArrayList<Integer> getHigh() {
        return high;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public void setMod(ArrayList<Integer> mod) {
        this.mod = mod;
    }

    public void setMin(ArrayList<Integer> min) {
        this.min = min;
    }

    public void setHigh(ArrayList<Integer> high) {
        this.high = high;
    }
}
