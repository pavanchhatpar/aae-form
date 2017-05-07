package com.psychapps.aaeform.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psychapps.aaeform.R;
import com.psychapps.aaeform.models.Form;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    FirebaseAuth.AuthStateListener fAuthStateListener;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(fAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(fAuthStateListener != null) {
            fAuth.removeAuthStateListener(fAuthStateListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        fAuth = FirebaseAuth.getInstance();
        fAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    if(user.getDisplayName() != null) {
                        Log.d("Firebase Auth", "user " + user.getDisplayName() + " signed in");
                    } else {
                        Log.d("Firebase Auth", "user " + user.getEmail() + " signed in");
                    }
                    databaseReference.child("f_skeleton").keepSynced(true);
                    databaseReference.child("formResponses").keepSynced(true);
                    databaseReference.child("forms").keepSynced(true);
                } else {
                    Log.d("Firebase Auth", "user signed out");
                }
            }
        };
        sp = getSharedPreferences(LoginActivity.PREF_FILE, Context.MODE_PRIVATE);
        final String email = sp.getString(LoginActivity.EMAIL_ID, null);
        if(fAuth.getCurrentUser().getDisplayName() != null ) {
            collapsingToolbar.setTitle(fAuth.getCurrentUser().getDisplayName());
        } else {
            collapsingToolbar.setTitle("Profile");
        }
        TextView emailView = (TextView)findViewById(R.id.email_id);
        emailView.setText(email);
        final TextView mineView = (TextView)findViewById(R.id.mine);
        final TextView totalView = (TextView)findViewById(R.id.total);
        mineView.setText("You filled 0 forms");
        totalView.setText("Total, 0 forms filled");
        databaseReference.child("forms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Form form;
                int mine= 0, total=0;
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    form = ds.getValue(Form.class);
                    if(form.getEmailid().equals(email)) mine++;
                    total++;
                }
                mineView.setText("You filled " + mine + " forms");
                totalView.setText("Total, " + total + " forms filled");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadForm();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, "Record new form", Snackbar.LENGTH_LONG)
                        .setAction("Record", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadForm();
                            }
                        }).show();
                return true;
            }
        });
        CardView logout = (CardView) findViewById(R.id.logout_card);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spe = sp.edit();
                spe.remove(LoginActivity.EMAIL_ID);
                spe.remove(LoginActivity.PASSWORD);
                spe.apply();
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                finish();
                fAuth.signOut();
                startActivity(intent);
            }
        });
    }

    private void loadForm() {
        Intent intent = new Intent(getBaseContext(), FormActivity.class);
        startActivity(intent);
    }

}
