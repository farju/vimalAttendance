package com.example.vimal.projectproposal;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    DatabaseReference mDatabase;
    //FirebaseAuth mauth;
    User me;
    boolean flag;
    Intent classIntent;
    ArrayList<String> student_names;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classdetails);
        classIntent = getIntent();
        classes = (Class) classIntent.getSerializableExtra("class");
        student_names = new ArrayList<String>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        TextView name = (TextView) findViewById(R.id.classDetailstext);
        flag = false;
        //mauth = FirebaseAuth.getInstance();

        renderUI();


        name.setText("Class Name: " + classes.getClass_name() + "\nCourse Code: " + classes.getCourse_code() + "\nRoom Number: " + classes.getRoom_num() + "\nClass Time: " + classes.getClass_time() + "\nDay of the Week: " + classes.getClass_date());

        //String type = (String) classIntent.getSerializableExtra("UserType");
    }

    private void renderUI() {
        //student_names.add("");
        final String UID = classIntent.getStringExtra("UID");
        Log.d("UID", UID);
        FirebaseDatabase.getInstance().getReference().child("users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                me = dataSnapshot.getValue(User.class);
                Log.d("user", "hi" + me.getFirst_name());

                if (me.getType().equals("Teacher")) {
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
                    if (classIntent.hasExtra("attendance_ID")) {
                        final String AID = classIntent.getStringExtra("attendance_ID");
                        final EditText code_ = (EditText) findViewById(R.id.confirm_code);
                        Button student_b = (Button) findViewById(R.id.confirm_button);
                        TextView txt = (TextView) findViewById(R.id.student_code_title);

                        code_.setVisibility(View.VISIBLE);
                        student_b.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.VISIBLE);

                        student_b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(TeacherClassViewActivity.this,classes.getCode(),Toast.LENGTH_LONG).show();
                                if (classes.getCode() != null && code_.getText().toString().equals(classes.getCode())) {
                                    if(classes.isAttendance_open()){
                                        //TODO: add student to existing attendance obj
                                        String A_key = mDatabase.child("attendance").child(AID).child("attendance_list").push().getKey();
                                        mDatabase.child("attendance").child(AID).child("attendance_list").child(A_key).setValue(UID);
                                        Toast.makeText(TeacherClassViewActivity.this,"You're Here!",Toast.LENGTH_LONG).show();

                                    }else{
                                        Toast.makeText(TeacherClassViewActivity.this,"You missed the window of opportunity to attend this class.",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //return to login
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

    public void onDialogPositiveClick(DialogFragment dialog, String code){
        classes.setCode(code);
        //
        Attendance attendance = new Attendance(classes.getClass_ID(), classes.getCode());
        // push attendance obj to firebase
        String AID = mDatabase.child("attendance").push().getKey();
        mDatabase.child("attendance").child(AID).setValue(attendance);

        System.out.println("hello " + classes.getStudentEmails());
        //send_invite(AID);
        classes.start_attendance();

        Intent userIntent = new Intent(TeacherClassViewActivity.this, AddStudentActivity.class);
        userIntent.putExtra("class", classes);
        userIntent.putExtra("attendance_ID", AID);
        startActivity(userIntent);

    }




}
