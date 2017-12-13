package com.example.vimal.projectproposal;

import android.os.CountDownTimer;
import android.util.Log;

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
    //private ArrayList<Attendance> attendance;

    public Class() {
        class_name = class_ID = course_code = room_num = teacher_UID = class_time = class_date = "";
        studentList = new HashMap<String, String>();
        attendance_open = false;
        code = null;
    }

    public Class(String id, String name, String code, String room, String teacher_id, String time, String date) {
        attendance_open = false;
        class_ID = id;
        class_name = name;
        course_code = code;
        room_num = room;
        teacher_UID = teacher_id;
        class_time = time;
        code = null;
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

    public ArrayList<String> getStudentIDs() {
        ArrayList<String> s = new ArrayList<>();
        Iterator myVeryOwnIterator = studentList.keySet().iterator();
        while (myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();
            s.add(studentList.get(key));
            }

        return s;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void start_attendance(){
        attendance_open = true;
        //TODO:code logic to record student attendance
        new CountDownTimer(30000, 1000) { //30 secs  -> 900000 is 15 mins (so vimal says)
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                attendance_open = false;
                Log.d("end", "timer finished " + attendance_open + " " + code);
                code = null;
            }
        }.start();

    }

    /*

    public List<User> getStudentList() {
        return studentList;
    }*/
}
