package com.psychapps.aaeform;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by pavan on 04/05/2017.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
