package com.psychapps.aaeform;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.database.FirebaseDatabase;
import com.psychapps.aaeform.controllers.FirebaseService;

/**
 * Created by pavan on 04/05/2017.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        startService(new Intent(MyApplication.this, FirebaseService.class));
    }
}
