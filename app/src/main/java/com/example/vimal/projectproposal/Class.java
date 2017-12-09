package com.example.vimal.projectproposal;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vimal on 12/3/2017.
 */

public class Class implements Serializable{
    private String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private String class_name;
    private String course_code;
    private String room_num;
    //private List<User> studentList;
    private String teacher_UID;
    private String class_time;
    private String class_date;
    //potentially add the attendance set in case I goofed up

    public Class( String id) {
        class_name = "";
        course_code = "";
        room_num = "";
        teacher_UID = id;
        class_time = "";
        class_date = "";

    }

    public Class(String name, String code, String room, String id, String time, String date) {
        class_name = name;
        course_code = code;
        room_num = room;
        teacher_UID = id;
        class_time = time;
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

    /*

    public List<User> getStudentList() {
        return studentList;
    }*/
}
