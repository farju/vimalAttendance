package com.example.vimal.projectproposal;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Vimal on 12/3/2017.
 */

public class Class implements Serializable{
    private String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private String class_name;
    private String class_ID;
    private String course_code;
    private String room_num;
    private String teacher_UID;
    private String class_time;
    private String class_date;
    private HashMap<String, String> studentList;
    private boolean attendance_open;
    private String code;
    private String emailList;
    //private ArrayList<Attendance> attendance;

    public Class() {
        class_name = class_ID = course_code = room_num = teacher_UID = class_time = class_date = "";
        studentList = new HashMap<String, String>();
        attendance_open = false;
        code = "";
        emailList = "";
    }

    public Class(String id, String name, String code, String room, String teacher_id, String time, String date) {
        attendance_open = false;
        class_ID = id;
        class_name = name;
        course_code = code;
        room_num = room;
        teacher_UID = teacher_id;
        class_time = time;
        code = "";
        boolean flag = false;
        for (int i = 0; i < days.length; i++) {
            if (date.equals(days[i])) {
                class_date = date;
                i = days.length;
                flag = true;
            }
        }

        if (!flag) {
            class_date = "";
        }
        studentList = new HashMap<String, String>();
        emailList = "";
        //figure out how to parse this data
    }
    public String getCourse_code() {
        return course_code;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getRoom_num() {
        return room_num;
    }

    public String getTeacher() {
        return teacher_UID;
    }

    public String getClass_time() {
        return class_time;
    }

    public String getClass_date() {
        return class_date;
    }

    public String getClass_ID() {return class_ID;}

    public HashMap<String, String> getStudentList() {
        return studentList;
    }

    public boolean isAttendance_open() {
        return attendance_open;
    }

    public void setAttendance_open(boolean attendance_open) {
        this.attendance_open = attendance_open;
    }

    public void setEmailList(String email) {
        emailList += email + ";";
        System.out.println("setEmailList is being called for: " + emailList);
    }

    public ArrayList<String> getStudentIDs() {
        ArrayList<String> s = new ArrayList<>();
        Iterator myVeryOwnIterator = studentList.keySet().iterator();
        while (myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();
            s.add(studentList.get(key));
            }

        return s;
    }

    public void addStudentEmails(){
        //final String s = "";
        Iterator myVeryOwnIterator = studentList.keySet().iterator();
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

        while (myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();//.next();
            //s.add(studentList.get(key));
            String val = studentList.get(key);
            FirebaseDatabase.getInstance().getReference().child("users").child(val).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    System.out.println("User email: " + user.getEmail());
                    setEmailList(user.getEmail());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
    public String getCode() {
        return code;
    }

    public void setCode(String c) {
        this.code = c;
        FirebaseDatabase.getInstance().getReference().child("classes").child(class_ID).child("code").setValue(c);
    }

    //Had to move this to TeacherClassViewActivity.java since cannot communicate with the activity properly
/*
    public void start_attendance(final Context c){
        final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        attendance_open = true;
        mData.child("classes").child(class_ID).child("attendance_open").setValue(true);

        new CountDownTimer(3000, 1000) { //30 secs  -> 900000 is 15 mins (so vimal says)
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                attendance_open = false;
                mData.child("classes").child(class_ID).child("attendance_open").setValue(false);
                Toast.makeText(c, "timer finished for the attendance event!", Toast.LENGTH_LONG).show();
                setCode("");
            }
        }.start();
    }
*/
    public String getEmailList() {
        System.out.println("Final email list: " + emailList);
        return emailList;

    }

    /*

    public List<User> getStudentList() {
        return studentList;
    }*/
}
