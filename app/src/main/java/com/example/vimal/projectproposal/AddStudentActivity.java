package com.example.vimal.projectproposal;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

public class AddStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //changed layout since it doesnt render any specific screen at runtime

        Intent intent = getIntent();
        Class classes = (Class) intent.getSerializableExtra("class");
        String classID = (String) classes.getClass_ID();
        String className = (String) classes.getClass_name();
        sendEmail(classID, className);


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
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Welcome to " + className + ".To join this class please click the link below.<br><a href=\"" + link_val + "\">Join class</a>") );

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AddStudentActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
