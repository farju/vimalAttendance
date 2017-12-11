package com.example.vimal.projectproposal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vimal on 12/6/2017.
 */

//This class is used to render the class creation screen for teachers
public class TeacherAddClass extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText Cname;
    private EditText Croom;
    private EditText Ccode;
    private EditText Ctime;
    private EditText Cdate;
    private String UID;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firsttimeteacher);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Cname = (EditText) findViewById(R.id.studentClassName);
        Croom = (EditText) findViewById(R.id.room_num);
        Ccode = (EditText) findViewById(R.id.course_code);
        Ctime = (EditText) findViewById(R.id.class_time);
        Cdate = (EditText) findViewById(R.id.day);

        UID = (String) getIntent().getSerializableExtra("UID");
        Log.d("hello", UID);

        Ctime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Ctime.setHint("HH:MM");
            }
        });
        Cdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Cdate.setHint("Monday, Tuesday, etc...");
            }
        });

        findViewById(R.id.addclass).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //https://developer.android.com/training/appbar/actions.html was used for this method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(this, MainActivity.class);
                startActivity(login);

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void updateData(String UID, final String class_id) {
        /*FirebaseDatabase.getInstance().getReference().child("users").child(UID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    //OnDataChange uses a Datasnapshot object, that represents the user object, and allows us to parse it for the values
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);
                        user.getClassList().add(class_id);
                        TextView welcome = (TextView) findViewById(R.id.welcome);
                    }

                    //if the database retrieval fails then program code in here
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.addclass) {
            if (validateInput(Cname.getText().toString(), Croom.getText().toString(), Ccode.getText().toString(), Ctime.getText().toString(), Cdate.getText().toString())) {
                Intent classIntent = new Intent(TeacherAddClass.this, TeacherInitialScreen.class);
                classIntent.putExtra("UID", UID);
                startActivity(classIntent);
            }
        }
    }

    public boolean validateInput(String name, String room, String code, String time, String date) {
        boolean valid = true;
        Log.d("name", name);
        Log.d("room", room);
        Log.d("code", code);
        Log.d("time", time);
        Log.d("date", date);
        //Can always modify the validation checks below but for now its checking empty or not
        if (TextUtils.isEmpty(name)) {
            Cname.setError("Required.");
            valid = false;
        } else {
            Cname.setError(null);
        }

        if (TextUtils.isEmpty(room)) {
            Croom.setError("Required.");
            valid = false;
        } else {
            Croom.setError(null);
        }

        if (TextUtils.isEmpty(code)) {
            Ccode.setError("Required.");
            valid = false;
        } else {
            Ccode.setError(null);
        }

        if (TextUtils.isEmpty(time)) {
            Ctime.setError("Required.");
            valid = false;
        } else {
            Ctime.setError(null);
        }

        if (TextUtils.isEmpty(date)) {
            Cdate.setError("Required.");
            valid = false;
        } else {
            Cdate.setError(null);
        }

        if (valid) {
            //TODO pass in class ID into this constructor
            Class class_ = new Class(name, code, room, UID, time, date);
            try {
                String CID = mDatabase.child("classes").push().getKey(); //GENERATE RANDOM STRING ID FOR CLASSES IN CONSTRUCTOR
                mDatabase.child("classes").child(CID).setValue(class_);
                mDatabase.child("users").child(UID).child("classList").push().setValue(CID);
                Log.d("Pushed Key", CID);
            } catch (Exception e) {
                Log.e("bad news", e.toString());
            }
        }
        return valid;
    }
}
