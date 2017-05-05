package com.psychapps.aaeform.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psychapps.aaeform.R;
import com.psychapps.aaeform.models.Form;
import com.psychapps.aaeform.models.FormResponse;
import com.psychapps.aaeform.models.FormSkeleton;

import java.util.ArrayList;

public class FormActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private TableRow tableRow;
    private TextView criteria;
    private LinearLayout min, mod, high;
    private ProgressBar mProgress;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sp;
    Form newForm;
    ArrayList<FormResponse> formResponses = new ArrayList<FormResponse>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(LoginActivity.PREF_FILE, Context.MODE_PRIVATE);
        newForm = new Form(sp.getString(LoginActivity.EMAIL_ID, null));
        tableLayout = (TableLayout) findViewById(R.id.form_table);
        mProgress = (ProgressBar) findViewById(R.id.form_load_progress);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("f_skeleton").keepSynced(true);
        databaseReference.child("formResponses").keepSynced(true);
        databaseReference.child("f_skeleton").child("A").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    showProgress(false);
                    return;
                }
                formResponses.clear();
                addTitle("A. PATIENT CONSIDERATIONS");
                addFormRows(dataSnapshot, "A");
                databaseReference.child("f_skeleton").child("B").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() == null) {
                            showProgress(false);
                            return;
                        }
                        addTitle("B. DIAGNOSTIC AND TREATMENT CONSIDERATIONS");
                        addFormRows(dataSnapshot, "B");
                        databaseReference.child("f_skeleton").child("C").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue() == null) {
                                    showProgress(false);
                                    return;
                                }
                                addTitle("C. ADDITIONAL CONSIDERATIONS");
                                addFormRows(dataSnapshot, "C");
                                showProgress(false);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }

    private void confirmExit() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Discard Form")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        confirmExit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                confirmExit();
                break;
            case R.id.form_done:
                checkForm();
                break;
        }
        return true;
    }

    private void checkForm() {
        boolean valid = true;
        for(FormResponse fr: formResponses) {
            int sum = 0;
            for(int i : fr.getMin()) {
                sum+=i;
            }
            for(int i : fr.getMod()) {
                sum+=i;
            }
            for(int i : fr.getHigh()) {
                sum+=i;
            }
            if(sum==0) {
                valid = false;
                break;
            }
        }
        if(valid) preSubmitForm();
        else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Incomplete form")
                    .setMessage("Seems like the form isn't complete yet")
                    .setNeutralButton(android.R.string.ok, null)
                    .show();
        }
    }

    private void preSubmitForm() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Submitting Form...")
                .setMessage("Was the patient referred to a specialist?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        submitForm(true);
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        submitForm(false);
                    }

                })
                .show();
    }

    private void submitForm(boolean referred) {
        newForm.setReferredToSpl(referred);
        DatabaseReference fref = databaseReference.child("forms").push();
        String fid = fref.getKey();
        ArrayList<String> frs = new ArrayList<String>();
        for(FormResponse fr: formResponses) {
            fr.setForm(fid);
            DatabaseReference frref = databaseReference.child("formResponses").push();
            frs.add(frref.getKey());
            frref.setValue(fr);
        }
        newForm.setResponses(frs);
        fref.setValue(newForm);
        finish();
    }

    /*
    private void resetForm() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Reset Form")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (FormResponse fr : formResponses) {
                            fr.setMin(new ArrayList<Integer>());
                            fr.setMod(new ArrayList<Integer>());
                            fr.setHigh(new ArrayList<Integer>());
                        }
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
    */

    private void showProgress(boolean show) {
        mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void addTitle(String title) {
        TextView tv = new TextView(FormActivity.this);
        tv.setText(title);
        tv.setTextSize(22);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        tv.setTextColor(ContextCompat.getColor(FormActivity.this, android.R.color.black));
        tableLayout.addView(tv);
        RelativeLayout rel = new RelativeLayout(getBaseContext());
        rel.setBackgroundColor(ContextCompat.getColor(FormActivity.this, android.R.color.black));
        rel.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        tableLayout.addView(rel);
    }

    private void addFormRows(DataSnapshot dataSnapshot, String section) {
        int x=0;
        for(DataSnapshot ds: dataSnapshot.getChildren()) {
            FormSkeleton formSkeleton = ds.getValue(FormSkeleton.class);
            tableRow = (TableRow) getLayoutInflater().inflate(R.layout.form_table_row, null);
            criteria = (TextView) tableRow.findViewById(R.id.criteria);
            min = (LinearLayout) tableRow.findViewById(R.id.min);
            mod = (LinearLayout) tableRow.findViewById(R.id.mod);
            high = (LinearLayout) tableRow.findViewById(R.id.high);
            criteria.setText(formSkeleton.getCriteria_and_subcriteria());
            FormResponse formResponse = new FormResponse(section, ds.getKey(),
                    formSkeleton.getMinimal_difficulty().size(),
                    formSkeleton.getModerate_difficulty().size(),
                    formSkeleton.getHigh_difficulty().size());
            final int i = formResponses.size();
            formResponses.add(formResponse);
            formSkeleton.getMinimal_difficulty();
            int it=0;
            for (String minString : formSkeleton.getMinimal_difficulty()) {
                CheckBox cb = new CheckBox(FormActivity.this);
                cb.setText(minString);
                final int finalIt = it;
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ArrayList<Integer> min = formResponses.get(i).getMin();
                        min.set(finalIt, isChecked? 1: 0);
                        formResponses.get(i).setMin(min);
                    }
                });
                min.addView(cb);
                it++;
            }
            it=0;
            for (String minString : formSkeleton.getModerate_difficulty()) {
                CheckBox cb = new CheckBox(FormActivity.this);
                cb.setText(minString);
                final int finalIt = it;
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ArrayList<Integer> min = formResponses.get(i).getMod();
                        min.set(finalIt, isChecked? 1: 0);
                        formResponses.get(i).setMod(min);
                    }
                });
                mod.addView(cb);
                it++;
            }
            it=0;
            for (String minString : formSkeleton.getHigh_difficulty()) {
                CheckBox cb = new CheckBox(FormActivity.this);
                cb.setText(minString);
                final int finalIt = it;
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ArrayList<Integer> min = formResponses.get(i).getHigh();
                        min.set(finalIt, isChecked? 1: 0);
                        formResponses.get(i).setHigh(min);
                    }
                });
                high.addView(cb);
                it++;
            }
            tableRow.setBackgroundColor(ContextCompat.getColor(FormActivity.this, x%2==0 ? android.R.color.white: R.color.table_row_grey));
            tableLayout.addView(tableRow);
            RelativeLayout rel = new RelativeLayout(getBaseContext());
            rel.setBackgroundColor(ContextCompat.getColor(FormActivity.this, android.R.color.black));
            rel.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
            tableLayout.addView(rel);
            x++;
        }
    }

}
