package com.example.vimal.projectproposal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class TeacherClassViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classdetails);
        Intent classIntent = getIntent();
        final Class classes = (Class) classIntent.getSerializableExtra("class");

        TextView name = (TextView) findViewById(R.id.classDetailstext);

        name.setText("Class Name: " + classes.getClass_name() + "\nCourse Code: " + classes.getCourse_code() + "\nRoom Number: " + classes.getRoom_num() + "\nClass Time: " + classes.getClass_time() + "\nDay of the Week: " + classes.getClass_date());

        String type = (String) classIntent.getSerializableExtra("UserType");
        if (type.equals("Teacher")) {
            Button b1 = (Button) findViewById(R.id.addStudents);
            Button b2 = (Button) findViewById(R.id.attendance_start);

            b1.setVisibility(View.VISIBLE);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent userIntent = new Intent(TeacherClassViewActivity.this, AddStudentActivity.class);
                    userIntent.putExtra("class", classes);
                    startActivity(userIntent);
                }
            });
            b2.setVisibility(View.VISIBLE);
            //TODO Starts attendance event
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent userIntent = new Intent(TeacherClassViewActivity.this, AttendanceCreation.class);
                    userIntent.putExtra("class", classes);
                    startActivity(userIntent);
                }
            });
        }

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
}
