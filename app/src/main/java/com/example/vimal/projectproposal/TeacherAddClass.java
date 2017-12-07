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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Vimal on 12/6/2017.
 */

//This class is used to render the class creation screen for teachers
public class TeacherAddClass extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firsttimeteacher);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String UID = (String) getIntent().getSerializableExtra("UID");
        Log.d("hello", UID);

        Class class_ = new Class(UID);

        try {
            String CID = mDatabase.child("classes").push().getKey();//GENERATE RANDOM STRING ID FOR CLASSES IN CONSTRUCTOR
            mDatabase.child("classes").child(CID).setValue(class_);
            //updateData(UID, class_.getClass_id());
            //update teacher to add class ID to classlist
            //mDatabase.child("users").child(UID).child("classList").push().getKey();
            mDatabase.child("users").child(UID).child("classList").push().setValue(CID);
            Log.d("Pushed Key", CID);
        } catch (Exception e) {
            Log.e("bad news", e.toString());
        }

        Button added = (Button) findViewById(R.id.addclass);
        added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherAddClass.this, TeacherInitialScreen.class));
            }
        });

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
}
