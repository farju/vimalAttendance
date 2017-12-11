package com.example.vimal.projectproposal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vimal on 12/3/2017.
 */

public class Class implements Serializable{
    private String class_id;
    private String course_code;
    private String classDetails;
    //private List<User> studentList;
    private String teacher_UID;
    //potentially add the attendance set in case I goofed up

    public Class() {}

    public Class( String id) {
        class_id = course_code = classDetails = "test class";
        teacher_UID = id;

    }
    public String getCourse_code() {
        return course_code;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getClassDetails() {
        return classDetails;
    }

    public String getTeacher() {
        return teacher_UID;
    }

}
