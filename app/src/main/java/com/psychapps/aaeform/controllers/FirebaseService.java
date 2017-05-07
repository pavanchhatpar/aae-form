package com.psychapps.aaeform.controllers;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.psychapps.aaeform.R;

public class FirebaseService extends Service {

    public class FirebaseBinder extends Binder {
        FirebaseService getService() {
            return FirebaseService.this;
        }
    }
    public FirebaseService() {
    }
    private final IBinder mBinder = new FirebaseBinder();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    @Override
    public void onCreate() {
        super.onCreate();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Firebase service", "Triggered onStartCommand");
        databaseReference.child("f_skeleton").keepSynced(true);
        databaseReference.child("formResponses").keepSynced(true);
        databaseReference.child("forms").keepSynced(true);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getBaseContext(), getString(R.string.firebase_stopped), Toast.LENGTH_SHORT).show();
        Log.i("Firebase service", getString(R.string.firebase_stopped));
    }
}
