package com.example.vimal.projectproposal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by Vimal on 12/15/2017.
 */

public class AttendanceDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.closeattendance);
        Intent intent = getIntent();
        TextView text = (TextView) findViewById(R.id.attendanceMessage);
        text.setText(intent.getStringExtra("message"));

        String AID = intent.getStringExtra("attendance_ID");
        addStudents(AID);
        Log.d("AID in atendance", AID);

        FirebaseDatabase.getInstance().getReference().child("attendance").child(AID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final LinearLayout ll = (LinearLayout) findViewById(R.id.attendance_student_list);
                Attendance attendance = dataSnapshot.getValue(Attendance.class);
                Log.d("atendance obj", attendance.getClassID());
                if(attendance.getAttendance() != null){
                    Iterator myVeryOwnIterator = attendance.getAttendance().keySet().iterator();

                    while (myVeryOwnIterator.hasNext()) {
                        final TextView studentName = new TextView(AttendanceDetails.this);
                        String key = (String) myVeryOwnIterator.next();
                        String studentID = (String) attendance.getAttendance().get(key);

                        FirebaseDatabase.getInstance().getReference().child("users").child(studentID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                studentName.setText(user.toString());
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        ll.addView(studentName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addStudents(String AID) {


    }
}
