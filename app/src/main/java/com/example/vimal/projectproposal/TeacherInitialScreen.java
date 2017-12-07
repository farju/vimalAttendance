package com.example.vimal.projectproposal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Vimal on 12/3/2017.
 */

public class TeacherInitialScreen extends AppCompatActivity {
    private DatabaseReference mDatabase;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacherviewclass);
        //Receive UID here
        final String UID = (String) getIntent().getSerializableExtra("UID");

        //used for instantiating the firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //this function is used to read data from the firebase database
        loadData(UID);

        //used to add a class and redirects to TeacherAddClass.java
        Button b = (Button) findViewById(R.id.addclass);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userIntent = new Intent(TeacherInitialScreen.this, TeacherAddClass.class);
                userIntent.putExtra("UID", UID);
                startActivity(userIntent);
            }
        });
        //Create the Array to receive all class names and when adding to listview we hardcode the creation of a new button
        //store teacher UID in the database when storing class objects (foreign key)
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
    private void loadData(String UID) {
        FirebaseDatabase.getInstance().getReference().child("users").child(UID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    //OnDataChange uses a Datasnapshot object, that represents the user object, and allows us to parse it for the values
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        User user = (User) dataSnapshot.getValue(User.class);
                        TextView welcome = (TextView) findViewById(R.id.welcome);
                        welcome.setText("Welcome " + user.getFirst_name() + " " + user.getLast_name() + " to your class list screen!");

                        //TODO: READ DATA FOR EACH CID AND PULL CLASS INFORMATION

                        //This code is to dynamically add the classes if they exist in the classList already
                        if (user.getClassList()!=null) {
                            //Didn't test this code as yet
                            RelativeLayout rl=(RelativeLayout)findViewById(R.id.teacherWelcome);

                            ScrollView sv = new ScrollView(TeacherInitialScreen.this);
                            sv.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));
                            LinearLayout ll = new LinearLayout(TeacherInitialScreen.this);
                            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                            ll.setOrientation(LinearLayout.VERTICAL);
                            sv.addView(ll);

                            //for loop to create the textview with the course code and the button along with it
                            for(int i = 0; i < user.getClassList().size(); i++) {
                                TextView text = new TextView(TeacherInitialScreen.this);
                                Button b = new Button(TeacherInitialScreen.this);
                                text.setText(user.getClassList().get(i));
                                //if the user is a student then allow them to view class not create attendance event
                                if (user.getType().equals("Student")) {
                                    b.setText("View Class");
                                } else {
                                    b.setText("Attendance");
                                }
                                ll.addView(text);
                                ll.addView(b);
                            }
                            rl.addView(sv);
                        }

                    }

                    //if the database retrieval fails then program code in here
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
