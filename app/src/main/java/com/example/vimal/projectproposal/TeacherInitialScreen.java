package com.example.vimal.projectproposal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Vimal on 12/3/2017.
 */

public class TeacherInitialScreen extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacherviewclass);
        String UID = (String) getIntent().getSerializableExtra("UID");
        Log.d("Hello", UID);

        //Create the Array to receive all class names and when adding to listview we hardcode the creation of a new button
        //store teacher UID in the database when storing class objects (foreign key)


    }
}
