package com.example.vimal.projectproposal;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mera on 2017-12-12.
 */

public class Attendance {
    private String code;
    private String classID;
    private HashMap<String,String> attendance_list; // student_id , N/N

    public Attendance(){};

    public Attendance(String class_, String c){
        classID = class_;
        code = c;
        attendance_list = new HashMap<String, String>();
    }

    public String getCode() {
        return code;
    }

    public HashMap<String, String> getAttendance() {
        return attendance_list;
    }

    public String getClassID() {
        return classID;
    }

    //public void add_attendance_record(String Student_ID) {attendance.add(Student_ID);}

}
