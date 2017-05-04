package com.psychapps.aaeform.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psychapps.aaeform.R;
import com.psychapps.aaeform.entities.Form;
import com.psychapps.aaeform.entities.User;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    User user;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        sp = getSharedPreferences(LoginActivity.PREF_FILE, Context.MODE_PRIVATE);
        final String email = sp.getString(LoginActivity.EMAIL_ID, null);
        databaseReference.child("users").child(email.replaceAll("\\.", "__dot__")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                collapsingToolbar.setTitle(user.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        TextView emailView = (TextView)findViewById(R.id.email_id);
        emailView.setText(email);
        final TextView mineView = (TextView)findViewById(R.id.mine);
        final TextView totalView = (TextView)findViewById(R.id.total);
        final int[] mine = {0};
        final int[] total = {0};
        mineView.setText("You filled " + mine[0] + " forms");
        totalView.setText("Total, " + total[0] + " forms filled");
        databaseReference.child("forms").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Form form;
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    form = ds.getValue(Form.class);
                    if(form.getEmailid().equals(email)) mine[0]++;
                    total[0]++;
                }
                mineView.setText("You filled " + mine[0] + " forms");
                totalView.setText("Total, " + total[0] + " forms filled");
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
                return false;
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
                startActivity(intent);
            }
        });
    }

    private void loadForm() {

    }

}
