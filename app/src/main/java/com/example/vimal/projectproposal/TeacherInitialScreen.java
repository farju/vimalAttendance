package com.example.vimal.projectproposal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
import java.util.Iterator;
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

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

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
                        User user = dataSnapshot.getValue(User.class);
                        TextView welcome = (TextView) findViewById(R.id.welcome);
                        welcome.setText("Welcome " + user.getFirst_name() + " " + user.getLast_name() + " to your class list screen!");

                        if (user.getType().equals("Teacher")) {
                            Button button = (Button) findViewById(R.id.addclass);
                            button.setVisibility(View.VISIBLE);
                            TextView addClass = (TextView) findViewById(R.id.addclassText);
                            addClass.setVisibility(View.VISIBLE);
                        }

                        if (user.getClassList()!=null) {
                            //packaged the layout updating class code into this function
                            renderClasses(user);
                        }

                    }

                    //if the database retrieval fails then program code in here
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Error", "User Database Retrieval Failed!");
                    }
                });
    }
//This function is used to dynamically add the classes for each user to the layout with a button
    private void renderClasses(final User user) {


        RelativeLayout rl=(RelativeLayout)findViewById(R.id.teacherWelcome);

        ScrollView sv = new ScrollView(TeacherInitialScreen.this);
        sv.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.WRAP_CONTENT, ScrollView.LayoutParams.WRAP_CONTENT));
        sv.getPaddingTop();
        LinearLayout ll = new LinearLayout(TeacherInitialScreen.this);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        //Iterator used to loop through each element of the hash map and collect the key and for each key get the value
            Iterator myVeryOwnIterator = user.getClassList().keySet().iterator();
            while (myVeryOwnIterator.hasNext()) {
                final TextView text = new TextView(TeacherInitialScreen.this);
                final Button b = new Button(TeacherInitialScreen.this);
                String key = (String) myVeryOwnIterator.next();
                String value = (String) user.getClassList().get(key);


                //Query database for information for each class object and dynamically add it to the layout
                FirebaseDatabase.getInstance().getReference().child("classes").child(value)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get class information
                                final Class classes = dataSnapshot.getValue(Class.class);
                                text.setText(classes.getClass_name());
                                if (user.getType().equals("Student")) {

                                    b.setText("View Class");
                                    b.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent registerIntent = new Intent(TeacherInitialScreen.this, TeacherClassViewActivity.class);
                                            registerIntent.putExtra("class", classes);
                                            registerIntent.putExtra("UserType", user.getType());
                                            startActivity(registerIntent);
                                        }
                                    });
                                } else {
                                    b.setText("Attendance");
                                    b.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent registerIntent = new Intent(TeacherInitialScreen.this, TeacherClassViewActivity.class);
                                            registerIntent.putExtra("class", classes);
                                            registerIntent.putExtra("UserType", user.getType());
                                            startActivity(registerIntent);
                                        }
                                    });

                                }
                            }

                            //if the database retrieval fails then program code in here
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("Error", "Class Database Retrieval Failed!");
                            }
                        });

                ll.addView(text);
                ll.addView(b);
            }
        rl.addView(sv);
    }
}
