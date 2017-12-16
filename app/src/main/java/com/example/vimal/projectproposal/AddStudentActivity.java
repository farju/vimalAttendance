package com.example.vimal.projectproposal;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

public class AddStudentActivity extends AppCompatActivity {
    Class classes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //changed layout since it doesnt render any specific screen at runtime

        Intent intent = getIntent();
        classes = (Class) intent.getSerializableExtra("class");
        String classID = (String) classes.getClass_ID();
        String className = (String) classes.getClass_name();
        String emails = (String) classes.Emails();
        if(intent.hasExtra("attendance_ID")){
            send_invite(intent.getStringExtra("attendance_ID"));
        }else{
            sendEmail(classID, className);
        }
    }

    private void sendEmail(String classID, String className) {
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        String link_val = "https://class/" + classID;
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Adding you to the " + className + " class");
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Welcome to " + className + ".To join this class please click the link below." +
                "<br><a href=\"" + link_val + "\">" + link_val + "</a>") );

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AddStudentActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void send_invite(String AID){
        Log.i("Send email", "");

        //TODO still have to figure out how to pull all the email addresses for the students here
        String emails = classes.Emails();
        Log.d("Emails", emails);
        String[] TO = {emails}; //student emails
        String[] CC = {""};
        String link_val = "https://attendance/" + AID;
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Attendance open for " + classes.getClass_name());
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Attendance is open for this class. You have a limited time to confirm your presence. " +
                "Click the link below and enter the confirmation code: <br><a href=\"" + link_val + "\">" + link_val + "</a>") );

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AddStudentActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
