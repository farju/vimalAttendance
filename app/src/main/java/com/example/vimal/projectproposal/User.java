package com.example.vimal.projectproposal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Vimal on 12/3/2017.
 */

public class User implements Serializable{
    private String last_name;
    private String first_name;
    private String email;
    private String type;
    private HashMap<String,String> classList;

    private User() {}

    public User(String Email, String first, String last, String Type) {
        last_name = last;
        first_name = first;
        email = Email;
        type = Type;
        classList = new HashMap<String, String>();
    }

    public ArrayList<String> getClassListString() {
        return new ArrayList<String>(classList.values());
    }

    public String getLast_name() {
        return last_name;
    }
    public String getFirst_name() {
        return first_name;
    }
    public String getEmail() {
        return email;
    }
    public String getType() {
        return type;
    }
    public HashMap<String, String> getClassList() {
        return classList;
    }
}
