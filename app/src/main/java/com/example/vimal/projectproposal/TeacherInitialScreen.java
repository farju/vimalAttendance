package com.example.vimal.projectproposal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vimal on 12/3/2017.
 */

public class TeacherInitialScreen extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private User user;
    private ArrayList<String> class_name;
    //ArrayAdapter<String> arrayAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacherviewclass);
        class_name = new ArrayList<>();
        //Receive UID here
        final String UID = (String) getIntent().getSerializableExtra("UID");

        //used for instantiating the firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //this function is used to read data from the firebase database
        loadData(UID);
        //arrayAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, user.getClassList());
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
        FirebaseDatabase.getInstance().getReference().child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                    //OnDataChange uses a Datasnapshot object, that represents the user object, and allows us to parse it for the values
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        user = (User) dataSnapshot.getValue(User.class);
                        TextView welcome = (TextView) findViewById(R.id.welcome);
                        welcome.setText("Welcome " + user.getFirst_name() + " " + user.getLast_name() + " to your class list screen!");
                        getClassName(user.getClassListString());
                    }

                    //if the database retrieval fails then program code in here
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void getClassName(ArrayList <String> CID){

        for(int i = 0; i < user.getClassList().size(); i++) {
            FirebaseDatabase.getInstance().getReference().child("classes").child(CID.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Class class_ = (Class) dataSnapshot.getValue(Class.class);
                    Log.d("Classa got: ", class_.getCourse_code());
                    class_name.add(class_.getCourse_code());
                    updateUI();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("loadClass:onCancelled", databaseError.toException().toString());
                }
            });
        }

    }

    private void updateUI() {
        //This code is to dynamically add the classes if they exist in the classList already
        if (user.getClassList()!=null) {
            LinearLayout ll = (LinearLayout) findViewById(R.id.linl);

            TextView text = new TextView(TeacherInitialScreen.this);

            //for each class ID get the class name
            int index = class_name.size() - 1;

            Log.d("Classa Key: ", user.getClassListString().get(index));
            text.setText(class_name.get(index));
            Log.d("Classa code: ", class_name.get(index));
            //if the user is a student then allow them to view class not create attendance event

            Button b = new Button(TeacherInitialScreen.this);
            if (user.getType().equals("Student")) {
                b.setText("View Class");
            } else {
                b.setText("Attendance");
            }
            ll.addView(text);
            ll.addView(b);
        }
    }

}
