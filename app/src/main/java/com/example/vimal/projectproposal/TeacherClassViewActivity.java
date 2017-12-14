package com.example.vimal.projectproposal;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeacherClassViewActivity extends AppCompatActivity implements AttendanceCreation.SetLimitDialogListener {

    ArrayAdapter<String> adapter;
    Class classes;
    Attendance attendance;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classdetails);
        Intent classIntent = getIntent();
        classes = (Class) classIntent.getSerializableExtra("class");
        final ArrayList<String> student_names = new ArrayList<String>();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bund = new Bundle();
                    bund.putSerializable("class", classes);
                    DialogFragment dialog = new AttendanceCreation();
                    dialog.setArguments(bund);
                    dialog.show(getFragmentManager(), "attendance");
                }
            });


            ArrayList<String> students_ID = classes.getStudentIDs();
            //if(students_ID.size() > 0) {
            for (int i = 0; i < students_ID.size(); i++) {
                FirebaseDatabase.getInstance().getReference().child("users").child(students_ID.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Log.d("Enrolled Students", user.toString());
                        student_names.add(user.toString());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            findViewById(R.id.attendance_title).setVisibility(View.VISIBLE);
            ListView students = (ListView) findViewById(R.id.student_list);
            students.setVisibility(View.VISIBLE);
            adapter = new ArrayAdapter<String>(TeacherClassViewActivity.this, android.R.layout.simple_list_item_1, student_names);
            students.setAdapter(adapter);

        }else{
            //TODO: set input text for student to confirm attendance
            final EditText code_ = (EditText) findViewById(R.id.confirm_code);
            Button student_b = (Button) findViewById(R.id.confirm_button);
            TextView txt = (TextView) findViewById(R.id.student_code_title);

            code_.setVisibility(View.VISIBLE);
            student_b.setVisibility(View.VISIBLE);
            txt.setVisibility(View.VISIBLE);

            student_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(classes.getCode()!= null && code_.getText().toString().equals(classes.getCode())){
                        //TODO: add student to existing attendance obj
                    }
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

    public void onDialogPositiveClick(DialogFragment dialog){

        //TODO: send notifications to all students in the class list and create attendance object with empty attendance list
        // push attendance obj to firebase
        attendance = new Attendance("", "");
        classes.start_attendance();
    }

}
